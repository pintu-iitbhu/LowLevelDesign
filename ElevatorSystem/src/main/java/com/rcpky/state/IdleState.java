package com.rcpky.state;

import lombok.extern.log4j.Log4j2;

/**
 * Represents the idle state of an elevator
 */
@Log4j2
public class IdleState implements ElevatorState {
    @Override
    public void handleRequest(ElevatorContext context) {
        // In idle state, the elevator is waiting for requests
        log.debug("Elevator is in idle state waiting for requests");
        
        // Transition logic can be implemented here if needed
        // For example, if a request comes in, transition to moving state
    }
}
