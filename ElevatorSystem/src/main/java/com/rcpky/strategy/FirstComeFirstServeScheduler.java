package com.rcpky.strategy;

import com.rcpky.elevator.ElevatorCar;
import com.rcpky.enums.Direction;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Scheduling strategy that prioritizes idle elevators and elevators already moving in the desired direction
 */
@Log4j2
@Service
public class FirstComeFirstServeScheduler implements ElevatorScheduler {

    @Override
    public ElevatorCar scheduleElevator(List<ElevatorCar> elevators, int floorNumber, Direction direction) {
        if (elevators == null || elevators.isEmpty()) {
            log.warn("No elevators available for scheduling");
            return null;
        }
        
        log.info("FCFS scheduler finding elevator for floor {} going {}", floorNumber, direction);
        
        // First priority: Idle elevator at the same floor
        for (ElevatorCar elevator : elevators) {
            if (!elevator.isUnderMaintenance() && 
                elevator.getCurrentDirection() == Direction.IDLE && 
                elevator.getCurrentFloor() == floorNumber) {
                
                log.info("Found idle elevator {} at the requested floor {}", 
                        elevator.getId(), floorNumber);
                return elevator;
            }
        }
        
        // Second priority: Elevator already moving in the same direction and will pass the floor
        for (ElevatorCar elevator : elevators) {
            if (!elevator.isUnderMaintenance()) {
                if (direction == Direction.UP && 
                    elevator.getCurrentDirection() == Direction.UP && 
                    elevator.getCurrentFloor() < floorNumber) {
                    
                    log.info("Found elevator {} moving UP and will pass floor {}", 
                            elevator.getId(), floorNumber);
                    return elevator;
                    
                } else if (direction == Direction.DOWN && 
                           elevator.getCurrentDirection() == Direction.DOWN && 
                           elevator.getCurrentFloor() > floorNumber) {
                    
                    log.info("Found elevator {} moving DOWN and will pass floor {}", 
                            elevator.getId(), floorNumber);
                    return elevator;
                }
            }
        }
        
        // Third priority: Any idle elevator
        for (ElevatorCar elevator : elevators) {
            if (!elevator.isUnderMaintenance() && elevator.getCurrentDirection() == Direction.IDLE) {
                log.info("Found idle elevator {} at floor {} for request at floor {}", 
                        elevator.getId(), elevator.getCurrentFloor(), floorNumber);
                return elevator;
            }
        }
        
        // Fourth priority: Any elevator with the least number of pending requests
        ElevatorCar leastBusyElevator = findLeastBusyElevator(elevators);
        if (leastBusyElevator != null) {
            log.info("Assigned least busy elevator {} at floor {} for request at floor {}", 
                    leastBusyElevator.getId(), leastBusyElevator.getCurrentFloor(), floorNumber);
            return leastBusyElevator;
        }
        
        log.warn("No suitable elevator found for floor {} going {}", floorNumber, direction);
        return null;
    }
    
    /**
     * Find the elevator with the least number of pending requests
     * 
     * @param elevators List of elevators to check
     * @return Elevator with the least number of pending requests, or null if all under maintenance
     */
    private ElevatorCar findLeastBusyElevator(List<ElevatorCar> elevators) {
        ElevatorCar leastBusy = null;
        int minRequests = Integer.MAX_VALUE;
        
        for (ElevatorCar elevator : elevators) {
            if (!elevator.isUnderMaintenance()) {
                // Using the sum of up and down queue sizes as a simple measure of busyness
                int totalRequests = elevator.getUpQueue().size() + elevator.getDownQueue().size();
                if (totalRequests < minRequests) {
                    minRequests = totalRequests;
                    leastBusy = elevator;
                }
            }
        }
        
        return leastBusy;
    }
}
