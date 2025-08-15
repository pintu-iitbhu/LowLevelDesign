package com.rcpky.strategy;

import com.rcpky.elevator.ElevatorCar;
import com.rcpky.enums.Direction;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Interface for elevator scheduling strategies
 */
@Component
public interface ElevatorScheduler {
    /**
     * Schedule an elevator to service a request from a specific floor in a specified direction
     * 
     * @param elevators List of available elevators
     * @param floorNumber Floor from which the request is made
     * @param direction Direction in which the user wants to go
     * @return Selected elevator to serve the request, or null if none available
     */
    ElevatorCar scheduleElevator(List<ElevatorCar> elevators, int floorNumber, Direction direction);
}
