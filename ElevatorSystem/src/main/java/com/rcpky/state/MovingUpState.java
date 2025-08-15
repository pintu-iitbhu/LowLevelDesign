package com.rcpky.state;

import lombok.extern.log4j.Log4j2;

/**
 * Represents the state when an elevator is moving upwards
 */
@Log4j2
public class MovingUpState implements ElevatorState {
    @Override
    public void handleRequest(ElevatorContext context) {
        // Logic for handling an elevator that is moving up
        log.debug("Elevator is moving upwards");
        
        // State transition logic would be handled in the ElevatorCar class
        // based on reaching destination floors or changing direction
    }
}
