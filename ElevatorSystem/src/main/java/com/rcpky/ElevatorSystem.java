package com.rcpky;

import com.rcpky.elevator.ElevatorCar;
import com.rcpky.elevator.ElevatorManager;
import com.rcpky.enums.Direction;
import com.rcpky.floors.FloorManager;
import com.rcpky.observer.ElevatorObserver;
import com.rcpky.strategy.ElevatorScheduler;
import com.rcpky.strategy.FirstComeFirstServeScheduler;
import com.rcpky.strategy.NearestCarScheduler;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Singleton class that manages the entire elevator system.
 * Coordinates all elevator operations, floor management, and scheduling.
 */
@Log4j2
@Getter
public class ElevatorSystem {
    private static ElevatorSystem instance;
    private final ElevatorManager elevatorManager;
    private final FloorManager floorManager;
    private ElevatorScheduler scheduler;
    private boolean isRunning;
    
    /**
     * Private constructor for singleton pattern
     * 
     * @param numElevators Number of elevators in the system
     * @param numFloors Number of floors in the building
     */
    private ElevatorSystem(int numElevators, int numFloors) {
        // Set instance early to avoid circular dependency when creating FloorManager
        instance = this;
        
        this.elevatorManager = new ElevatorManager(numElevators);
        this.floorManager = new FloorManager(numFloors, this);
        this.scheduler = new FirstComeFirstServeScheduler(); // Default scheduler
        this.isRunning = false;
        
        // Register FloorManager as an observer to all elevators
        registerObservers();
        
        log.info("Elevator System initialized with {} elevators and {} floors", 
                numElevators, numFloors);
    }
    
    /**
     * Get the singleton instance of ElevatorSystem
     * 
     * @param numElevators Number of elevators in the system
     * @param numFloors Number of floors in the building
     * @return The ElevatorSystem instance
     */
    public static synchronized ElevatorSystem getInstance(int numElevators, int numFloors) {
        if (instance == null) {
            instance = new ElevatorSystem(numElevators, numFloors);
        }
        return instance;
    }
    
    /**
     * Get the singleton instance (must be initialized first)
     * 
     * @return The ElevatorSystem instance
     */
    public static synchronized ElevatorSystem getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ElevatorSystem has not been initialized. " +
                                            "Call getInstance(numElevators, numFloors) first.");
        }
        return instance;
    }
    
    /**
     * Register FloorManager as observer to all elevators
     */
    private void registerObservers() {
        for (ElevatorCar elevator : elevatorManager.getElevators()) {
            elevator.registerObserver(floorManager);
        }
        log.info("Floor manager registered as observer to all elevators");
    }
    
    /**
     * Start the elevator system
     */
    public void start() {
        if (isRunning) {
            log.warn("Elevator system is already running");
            return;
        }
        
        elevatorManager.startAllElevators();
        isRunning = true;
        log.info("Elevator system started");
    }
    
    /**
     * Stop the elevator system
     */
    public void stop() {
        if (!isRunning) {
            log.warn("Elevator system is already stopped");
            return;
        }
        
        elevatorManager.stopAllElevators();
        isRunning = false;
        log.info("Elevator system stopped");
    }
    
    /**
     * Process a request from inside an elevator to go to a specific floor
     * 
     * @param elevatorId ID of the elevator
     * @param targetFloor Floor number to go to
     * @return true if request was successfully processed, false otherwise
     */
    public boolean processInternalRequest(int elevatorId, int targetFloor) {
        if (!isRunning) {
            log.warn("Cannot process request - system is not running");
            return false;
        }
        
        ElevatorCar elevator = elevatorManager.getElevator(elevatorId);
        if (elevator == null) {
            log.error("Cannot process internal request - elevator {} not found", elevatorId);
            return false;
        }
        
        if (elevator.isUnderMaintenance()) {
            log.warn("Cannot process internal request - elevator {} is under maintenance", elevatorId);
            return false;
        }
        
        int maxFloor = floorManager.getFloors().size() - 1;
        if (targetFloor < 0 || targetFloor > maxFloor) {
            log.error("Cannot process internal request - floor {} out of range [0-{}]", 
                    targetFloor, maxFloor);
            return false;
        }
        
        // Determine direction based on current floor and target floor
        Direction direction = Direction.IDLE;
        if (targetFloor > elevator.getCurrentFloor()) {
            direction = Direction.UP;
        } else if (targetFloor < elevator.getCurrentFloor()) {
            direction = Direction.DOWN;
        } else {
            log.info("Elevator {} already at floor {}, no action needed", elevatorId, targetFloor);
            return true;
        }
        
        // Add request to elevator's queue
        elevator.addFloorRequest(targetFloor, direction);
        log.info("Internal request processed: Elevator {} to floor {}", elevatorId, targetFloor);
        return true;
    }
    
    /**
     * Process a request from a floor to get an elevator
     * 
     * @param floorNumber Floor where the request originated
     * @param direction Direction in which the user wants to go
     * @return ID of the elevator assigned to the request, or -1 if no elevator available
     */
    public int processExternalRequest(int floorNumber, Direction direction) {
        if (!isRunning) {
            log.warn("Cannot process request - system is not running");
            return -1;
        }
        
        int maxFloor = floorManager.getFloors().size() - 1;
        if (floorNumber < 0 || floorNumber > maxFloor) {
            log.error("Cannot process external request - floor {} out of range [0-{}]", 
                    floorNumber, maxFloor);
            return -1;
        }
        
        if (direction == Direction.IDLE) {
            log.warn("Cannot process external request with IDLE direction");
            return -1;
        }
        
        // Use the scheduler to find the best elevator for this request
        ElevatorCar selectedElevator = scheduler.scheduleElevator(
                elevatorManager.getElevators(), floorNumber, direction);
        
        if (selectedElevator == null) {
            log.warn("No elevator available to serve request from floor {} going {}", 
                    floorNumber, direction);
            return -1;
        }
        
        // Add the request to the selected elevator
        selectedElevator.addFloorRequest(floorNumber, direction);
        log.info("External request from floor {} going {} assigned to elevator {}", 
                floorNumber, direction, selectedElevator.getId());
        
        // Update the display on the floor
        floorManager.updateDisplay(floorNumber, selectedElevator.getId(), 
                selectedElevator.getCurrentDirection(), "Elevator assigned");
        
        return selectedElevator.getId();
    }
    
    /**
     * Set a specific elevator to maintenance mode
     * 
     * @param elevatorId ID of the elevator
     * @param maintenance true to put in maintenance mode, false to return to service
     * @return true if successful, false if elevator not found
     */
    public boolean setElevatorMaintenance(int elevatorId, boolean maintenance) {
        return elevatorManager.setElevatorMaintenance(elevatorId, maintenance);
    }
    
    /**
     * Change the scheduling strategy
     * 
     * @param scheduler The new scheduling strategy to use
     */
    public void setScheduler(ElevatorScheduler scheduler) {
        this.scheduler = scheduler;
        log.info("Elevator scheduling strategy changed to {}", 
                scheduler.getClass().getSimpleName());
    }
    
    /**
     * Get the direction of a specific elevator
     * 
     * @param elevatorId ID of the elevator
     * @return Direction of the elevator, or null if elevator not found
     */
    public Direction getElevatorDirection(int elevatorId) {
        ElevatorCar elevator = elevatorManager.getElevator(elevatorId);
        if (elevator != null) {
            return elevator.getCurrentDirection();
        }
        return null;
    }
    
    /**
     * Register an observer to a specific elevator
     * 
     * @param elevatorId ID of the elevator
     * @param observer Observer to register
     * @return true if successful, false if elevator not found
     */
    public boolean registerElevatorObserver(int elevatorId, ElevatorObserver observer) {
        ElevatorCar elevator = elevatorManager.getElevator(elevatorId);
        if (elevator != null) {
            elevator.registerObserver(observer);
            return true;
        }
        return false;
    }
    
    /**
     * Main method to demonstrate the elevator system
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Create an elevator system with 3 elevators and 10 floors
        ElevatorSystem system = ElevatorSystem.getInstance(3, 10);
        
        // Start the system
        system.start();
        
        try {
            // Process some external requests
            system.processExternalRequest(5, Direction.UP);
            Thread.sleep(1000);
            system.processExternalRequest(2, Direction.DOWN);
            Thread.sleep(1000);
            
            // Process some internal requests
            system.processInternalRequest(0, 7);
            Thread.sleep(1000);
            system.processInternalRequest(1, 3);
            
            // Let the system run for a while
            Thread.sleep(10000);
            
            // Try changing the scheduling strategy
            system.setScheduler(new NearestCarScheduler());
            
            // Process more requests with the new scheduler
            system.processExternalRequest(8, Direction.DOWN);
            Thread.sleep(1000);
            system.processExternalRequest(1, Direction.UP);
            
            // Let the system run for a while longer
            Thread.sleep(10000);
            
            // Stop the system
            system.stop();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Main thread interrupted", e);
        }
    }
}