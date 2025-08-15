package com.rcpky.elevator;

import com.rcpky.enums.Direction;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all elevators in the system
 */
@Getter
@Log4j2
public class ElevatorManager {
    private final List<ElevatorCar> elevators;
    private final int defaultCapacity = 10;

    /**
     * Creates a manager with specified number of elevators
     * 
     * @param numberOfElevators Number of elevators to create
     */
    public ElevatorManager(int numberOfElevators) {
        if (numberOfElevators < 1) {
            throw new IllegalArgumentException("Building must have at least one elevator");
        }
        
        this.elevators = new ArrayList<>();
        
        // Initialize all elevators
        for (int i = 0; i < numberOfElevators; i++) {
            ElevatorCar elevator = new ElevatorCar(i, defaultCapacity);
            elevators.add(elevator);
        }
        
        log.info("ElevatorManager initialized with {} elevators", numberOfElevators);
    }

    /**
     * Get a specific elevator by its ID
     * 
     * @param id ID of the elevator to get
     * @return ElevatorCar with the specified ID, or null if not found
     */
    public ElevatorCar getElevator(int id) {
        for (ElevatorCar elevator : elevators) {
            if (elevator.getId() == id) {
                return elevator;
            }
        }
        log.warn("Elevator with ID {} not found", id);
        return null;
    }

    /**
     * Start all elevators in the system
     */
    public void startAllElevators() {
        for (ElevatorCar elevator : elevators) {
            elevator.start();
        }
        log.info("All elevators started");
    }

    /**
     * Stop all elevators in the system
     */
    public void stopAllElevators() {
        for (ElevatorCar elevator : elevators) {
            elevator.stop();
        }
        log.info("All elevators stopped");
    }

    /**
     * Set maintenance mode for a specific elevator
     * 
     * @param id ID of the elevator
     * @param maintenance true to put in maintenance mode, false to return to service
     * @return true if successful, false if elevator not found
     */
    public boolean setElevatorMaintenance(int id, boolean maintenance) {
        ElevatorCar elevator = getElevator(id);
        if (elevator != null) {
            elevator.setMaintenance(maintenance);
            log.info("Elevator {} maintenance mode set to: {}", id, maintenance);
            return true;
        }
        return false;
    }
    
    /**
     * Find the nearest available elevator to a floor
     * 
     * @param floorNumber The floor number
     * @return The nearest available elevator, or null if none available
     */
    public ElevatorCar getNearestElevator(int floorNumber) {
        ElevatorCar nearest = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (ElevatorCar elevator : elevators) {
            if (elevator.isUnderMaintenance()) {
                continue; // Skip elevators under maintenance
            }
            
            int distance = Math.abs(elevator.getCurrentFloor() - floorNumber);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = elevator;
            }
        }
        
        return nearest;
    }

    public List<ElevatorCar> getElevators() {
        return new ArrayList<>(elevators);
    }
    
    public List<ElevatorCar> getElevatorsByDirection(Direction direction) {
        List<ElevatorCar> result = new ArrayList<>();
        for (ElevatorCar elevator : elevators) {
            if (elevator.getCurrentDirection() == direction) {
                result.add(elevator);
            }
        }
        return result;
    }
    
    public List<ElevatorCar> getIdleElevators() {
        return getElevatorsByDirection(Direction.IDLE);
    }
    
    public void setElevatorOutOfService(int id) {
        ElevatorCar elevator = elevators.get(id);
        if (elevator != null) {
            elevator.stop();
            log.info("Elevator {} set out of service", id);
        }
    }
    
    public void setElevatorInService(int id) {
        ElevatorCar elevator = elevators.get(id);
        if (elevator != null) {
            elevator.start();
            log.info("Elevator {} set back in service", id);
        }
    }
}
