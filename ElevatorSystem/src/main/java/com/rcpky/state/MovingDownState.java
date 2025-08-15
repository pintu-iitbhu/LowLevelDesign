package com.rcpky.state;

import lombok.extern.log4j.Log4j2;

/**
 * Represents the state when an elevator is moving downwards
 */
@Log4j2
public class MovingDownState implements ElevatorState {
    @Override
    public void handleRequest(ElevatorContext context) {
        // Logic for handling an elevator that is moving down
        log.debug("Elevator is moving downwards");
        
        // State transition logic would be handled in the ElevatorCar class
        // based on reaching destination floors or changing direction
    }
}
