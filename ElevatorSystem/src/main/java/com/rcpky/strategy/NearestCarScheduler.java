package com.rcpky.strategy;

import com.rcpky.elevator.ElevatorCar;
import com.rcpky.enums.Direction;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Scheduling strategy that prioritizes elevators based on proximity to the requested floor
 * while considering direction and current workload
 */
@Log4j2
@Service
public class NearestCarScheduler implements ElevatorScheduler {

    @Override
    public ElevatorCar scheduleElevator(List<ElevatorCar> elevators, int floorNumber, Direction direction) {
        if (elevators == null || elevators.isEmpty()) {
            log.warn("No elevators available for scheduling");
            return null;
        }
        
        log.info("Nearest car scheduler finding elevator for floor {} going {}", floorNumber, direction);
        
        // First check for elevators at the requested floor
        ElevatorCar elevatorAtFloor = findElevatorAtFloor(elevators, floorNumber);
        if (elevatorAtFloor != null) {
            log.info("Found elevator {} already at requested floor {}", 
                    elevatorAtFloor.getId(), floorNumber);
            return elevatorAtFloor;
        }
        
        // Check for elevators that are already heading towards the requested floor
        ElevatorCar elevatorEnRoute = findElevatorEnRoute(elevators, floorNumber, direction);
        if (elevatorEnRoute != null) {
            log.info("Found elevator {} en route to floor {} with matching direction", 
                    elevatorEnRoute.getId(), floorNumber);
            return elevatorEnRoute;
        }
        
        // Find the nearest idle elevator
        ElevatorCar nearestIdle = findNearestIdleElevator(elevators, floorNumber);
        if (nearestIdle != null) {
            log.info("Found nearest idle elevator {} at floor {}", 
                    nearestIdle.getId(), nearestIdle.getCurrentFloor());
            return nearestIdle;
        }
        
        // Last resort - find the nearest elevator with consideration for its workload
        ElevatorCar nearestAvailable = findNearestAvailableElevator(elevators, floorNumber);
        if (nearestAvailable != null) {
            log.info("Found nearest available elevator {} at floor {} with acceptable workload", 
                    nearestAvailable.getId(), nearestAvailable.getCurrentFloor());
            return nearestAvailable;
        }
        
        log.warn("No suitable elevator found for floor {} going {}", floorNumber, direction);
        return null;
    }
    
    /**
     * Find an elevator that is currently at the requested floor
     * 
     * @param elevators List of elevators to check
     * @param floorNumber Requested floor number
     * @return An elevator at the requested floor, or null if none found
     */
    private ElevatorCar findElevatorAtFloor(List<ElevatorCar> elevators, int floorNumber) {
        for (ElevatorCar elevator : elevators) {
            if (!elevator.isUnderMaintenance() && elevator.getCurrentFloor() == floorNumber) {
                if (elevator.getCurrentDirection() == Direction.IDLE) {
                    return elevator;
                }
            }
        }
        return null;
    }
    
    /**
     * Find an elevator that is already moving toward the requested floor
     * with the same direction
     * 
     * @param elevators List of elevators to check
     * @param floorNumber Requested floor number
     * @param direction Requested direction
     * @return An elevator heading toward the requested floor, or null if none found
     */
    private ElevatorCar findElevatorEnRoute(List<ElevatorCar> elevators, int floorNumber, Direction direction) {
        for (ElevatorCar elevator : elevators) {
            if (elevator.isUnderMaintenance()) {
                continue;
            }
            
            if (direction == Direction.UP && 
                elevator.getCurrentDirection() == Direction.UP && 
                elevator.getCurrentFloor() < floorNumber) {
                return elevator;
            } 
            else if (direction == Direction.DOWN && 
                     elevator.getCurrentDirection() == Direction.DOWN && 
                     elevator.getCurrentFloor() > floorNumber) {
                return elevator;
            }
        }
        return null;
    }
    
    /**
     * Find the nearest idle elevator to the requested floor
     * 
     * @param elevators List of elevators to check
     * @param floorNumber Requested floor number
     * @return The nearest idle elevator, or null if none available
     */
    private ElevatorCar findNearestIdleElevator(List<ElevatorCar> elevators, int floorNumber) {
        ElevatorCar nearest = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (ElevatorCar elevator : elevators) {
            if (!elevator.isUnderMaintenance() && elevator.getCurrentDirection() == Direction.IDLE) {
                int distance = Math.abs(elevator.getCurrentFloor() - floorNumber);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = elevator;
                }
            }
        }
        
        return nearest;
    }
    
    /**
     * Find the nearest elevator considering distance and workload
     * 
     * @param elevators List of elevators to check
     * @param floorNumber Requested floor number
     * @return The nearest available elevator, or null if none available
     */
    private ElevatorCar findNearestAvailableElevator(List<ElevatorCar> elevators, int floorNumber) {
        ElevatorCar bestElevator = null;
        double minScore = Double.MAX_VALUE;
        
        for (ElevatorCar elevator : elevators) {
            if (elevator.isUnderMaintenance()) {
                continue;
            }
            
            // Calculate score based on distance and number of pending requests
            int distance = Math.abs(elevator.getCurrentFloor() - floorNumber);
            int requestCount = elevator.getUpQueue().size() + elevator.getDownQueue().size();
            
            // Score = distance + (requestCount * weighting factor)
            // Higher weighting factor gives more importance to existing workload
            double score = distance + (requestCount * 1.5);
            
            if (score < minScore) {
                minScore = score;
                bestElevator = elevator;
            }
        }
        
        return bestElevator;
    }
}
