package com.rcpky.elevator;

import com.rcpky.Door;
import com.rcpky.display.Display;
import com.rcpky.enums.Direction;
import com.rcpky.observer.ElevatorObserver;
import com.rcpky.observer.ElevatorSubject;
import com.rcpky.panels.ElevatorPanel;
import com.rcpky.state.ElevatorContext;
import com.rcpky.state.IdleState;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Getter
@Log4j2
public class ElevatorCar implements Runnable, ElevatorSubject {
    private int id;
    private int currentFloor;
    private int capacity;
    private Direction currentDirection;
    private ElevatorContext stateContext;
    private ElevatorPanel elevatorPanel;
    private Display display;
    private Door door;
    private PriorityQueue<Integer> upQueue;
    private PriorityQueue<Integer> downQueue;
    private boolean isRunning;
    private Thread elevatorThread;
    private final List<ElevatorObserver> observers;
    private int currentLoad;
    private boolean isUnderMaintenance;

    public ElevatorCar(int id, int capacity) {
        this.id = id;
        this.currentFloor = 0; // Ground floor
        this.capacity = capacity;
        this.currentDirection = Direction.IDLE;
        this.stateContext = new ElevatorContext();
        this.stateContext.setState(new IdleState()); // Start in idle state
        this.door = new Door();
        this.display = new Display();
        this.elevatorPanel = new ElevatorPanel();
        this.observers = new ArrayList<>();
        this.currentLoad = 0;
        this.isUnderMaintenance = false;
        
        // Initialize queues
        this.upQueue = new PriorityQueue<>(); // Natural ordering for up (ascending)
        this.downQueue = new PriorityQueue<>((a, b) -> b - a); // Reverse order for down (descending)
        
        log.info("Elevator {} initialized at floor {}", id, currentFloor);
    }
    
    public void start() {
        if (elevatorThread == null || !elevatorThread.isAlive()) {
            isRunning = true;
            elevatorThread = new Thread(this);
            elevatorThread.start();
            log.info("Elevator {} started", id);
        }
    }
    
    public void stop() {
        isRunning = false;
        if (elevatorThread != null) {
            elevatorThread.interrupt();
            log.info("Elevator {} stopped", id);
        }
    }
    
    public void addFloorRequest(int targetFloor, Direction direction) {
        if (targetFloor == currentFloor) {
            log.info("Elevator {} already at floor {}", id, targetFloor);
            return;
        }
        
        if (direction == Direction.UP || targetFloor > currentFloor) {
            upQueue.add(targetFloor);
            log.info("Added floor {} to UP queue of elevator {}", targetFloor, id);
        } else if (direction == Direction.DOWN || targetFloor < currentFloor) {
            downQueue.add(targetFloor);
            log.info("Added floor {} to DOWN queue of elevator {}", targetFloor, id);
        }
        
        // If elevator is idle, set direction based on request
        if (currentDirection == Direction.IDLE) {
            if (!upQueue.isEmpty()) {
                currentDirection = Direction.UP;
            } else if (!downQueue.isEmpty()) {
                currentDirection = Direction.DOWN;
            }
        }
    }
    
    public void addToQueue(int targetFloor) {
        if (targetFloor == currentFloor) {
            log.info("Elevator {} already at floor {}", id, targetFloor);
            return;
        }
        
        if (targetFloor > currentFloor) {
            upQueue.add(targetFloor);
            log.info("Added floor {} to UP queue of elevator {}", targetFloor, id);
        } else {
            downQueue.add(targetFloor);
            log.info("Added floor {} to DOWN queue of elevator {}", targetFloor, id);
        }
        
        // If elevator is idle, set direction based on request
        if (currentDirection == Direction.IDLE) {
            if (!upQueue.isEmpty()) {
                currentDirection = Direction.UP;
            } else if (!downQueue.isEmpty()) {
                currentDirection = Direction.DOWN;
            }
        }
    }
    
    @Override
    public void run() {
        while (isRunning) {
            try {
                // Process the current state
                stateContext.handleRequest();
                
                // Process the elevator movement based on direction and requests
                if (!isUnderMaintenance) {
                    processMovement();
                } else {
                    log.info("Elevator {} is under maintenance. Not processing movement.", id);
                    Thread.sleep(5000); // Wait longer when in maintenance
                }
                
                // Simulate time taken for movement
                Thread.sleep(1000); // 1 second per operation
            } catch (InterruptedException e) {
                log.error("Elevator {} operation interrupted", id);
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void processMovement() {
        // Check if we need to stop at the current floor
        boolean shouldStop = shouldStopAtCurrentFloor();
        
        if (shouldStop) {
            stopAtFloor();
        } else {
            moveElevator();
        }
        
        // Update display
        display.showFloor(currentFloor);
        display.showDirection(currentDirection);
    }
    
    private boolean shouldStopAtCurrentFloor() {
        if (currentDirection == Direction.UP && upQueue.contains(currentFloor)) {
            return true;
        }
        if (currentDirection == Direction.DOWN && downQueue.contains(currentFloor)) {
            return true;
        }
        // If there are no more requests in current direction but there are in opposite,
        // change direction and check again
        if (currentDirection == Direction.UP && upQueue.isEmpty() && !downQueue.isEmpty()) {
            currentDirection = Direction.DOWN;
            return downQueue.contains(currentFloor);
        }
        if (currentDirection == Direction.DOWN && downQueue.isEmpty() && !upQueue.isEmpty()) {
            currentDirection = Direction.UP;
            return upQueue.contains(currentFloor);
        }
        return false;
    }
    
    private void stopAtFloor() {
        log.info("Elevator {} stopping at floor {}", id, currentFloor);
        
        // Notify observers of arrival
        notifyElevatorArrival(id, currentFloor);
        
        // Remove current floor from appropriate queue
        if (currentDirection == Direction.UP) {
            upQueue.remove(currentFloor);
            // Turn off the button light for this floor
            elevatorPanel.resetFloorButton(currentFloor);
        } else if (currentDirection == Direction.DOWN) {
            downQueue.remove(currentFloor);
            // Turn off the button light for this floor
            elevatorPanel.resetFloorButton(currentFloor);
        }
        
        // Open door
        door.open();
        notifyDoorOpened(id, currentFloor);
        log.info("Elevator {} doors opened at floor {}", id, currentFloor);
        
        try {
            // Wait for passengers to enter/exit
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Close door
        door.close();
        notifyDoorClosed(id, currentFloor);
        log.info("Elevator {} doors closed at floor {}", id, currentFloor);
        
        // Check if we need to change direction or go idle
        updateDirection();
    }
    
    private void moveElevator() {
        if (currentDirection == Direction.UP) {
            currentFloor++;
            log.info("Elevator {} moving UP to floor {}", id, currentFloor);
        } else if (currentDirection == Direction.DOWN) {
            currentFloor--;
            log.info("Elevator {} moving DOWN to floor {}", id, currentFloor);
        }
        // If IDLE, don't move
    }
    
    private void updateDirection() {
        if (currentDirection == Direction.UP && upQueue.isEmpty()) {
            if (!downQueue.isEmpty()) {
                currentDirection = Direction.DOWN;
                log.info("Elevator {} changing direction to DOWN", id);
            } else {
                currentDirection = Direction.IDLE;
                log.info("Elevator {} is now IDLE", id);
            }
        } else if (currentDirection == Direction.DOWN && downQueue.isEmpty()) {
            if (!upQueue.isEmpty()) {
                currentDirection = Direction.UP;
                log.info("Elevator {} changing direction to UP", id);
            } else {
                currentDirection = Direction.IDLE;
                log.info("Elevator {} is now IDLE", id);
            }
        }
    }
    
    public void setMaintenance(boolean maintenance) {
        this.isUnderMaintenance = maintenance;
        if (maintenance) {
            // Clear all requests
            upQueue.clear();
            downQueue.clear();
            currentDirection = Direction.IDLE;
            log.info("Elevator {} set to maintenance mode", id);
        } else {
            log.info("Elevator {} maintenance mode cleared", id);
        }
    }
    
    /**
     * Handle an emergency situation in the elevator
     */
    public void handleEmergency() {
        log.info("EMERGENCY triggered in elevator {} at floor {}", id, currentFloor);
        
        // Stop the elevator and set to maintenance mode
        stop();
        setMaintenance(true);
        
        // Ensure doors are opened for evacuation
        door.open();
        
        // Notify all observers about the emergency
        notifyEmergency(id, currentFloor);
        
        log.warn("Elevator {} in emergency mode. Maintenance required.", id);
    }
    
    /**
     * Set the current load of the elevator
     * 
     * @param load Number of passengers
     */
    public void setCurrentLoad(int load) {
        this.currentLoad = Math.min(load, capacity);
        log.info("Elevator {} load set to {}/{}", id, currentLoad, capacity);
    }
    
    public int getId() {
        return id;
    }
    
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    public Direction getCurrentDirection() {
        return currentDirection;
    }
    
    public boolean isBusy() {
        return currentDirection != Direction.IDLE;
    }
    
    // Observer pattern implementation
    /**
     * Register an observer for elevator events
     * 
     * @param observer Observer to register
     */
    public void registerObserver(ElevatorObserver observer) {
        addObserver(observer);
    }

    @Override
    public void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ElevatorObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyElevatorArrival(int elevatorId, int floorNumber) {
        for (ElevatorObserver observer : observers) {
            observer.onElevatorArrived(elevatorId, floorNumber);
        }
    }

    @Override
    public void notifyDoorOpened(int elevatorId, int floorNumber) {
        for (ElevatorObserver observer : observers) {
            observer.onDoorOpened(elevatorId, floorNumber);
        }
    }

    @Override
    public void notifyDoorClosed(int elevatorId, int floorNumber) {
        for (ElevatorObserver observer : observers) {
            observer.onDoorClosed(elevatorId, floorNumber);
        }
    }

    @Override
    public void notifyEmergency(int elevatorId, int floorNumber) {
        for (ElevatorObserver observer : observers) {
            observer.onEmergency(elevatorId, floorNumber);
        }
    }
}
