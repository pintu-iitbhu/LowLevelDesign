package com.rcpky.state;

import lombok.extern.log4j.Log4j2;

/**
 * Context class for managing elevator states
 */
@Log4j2
public class ElevatorContext {
    private ElevatorState currentState;
    
    public void setState(ElevatorState state) {
        this.currentState = state;
        log.info("Elevator state changed to: {}", state.getClass().getSimpleName());
    }
    
    public void handleRequest() {
        if (currentState != null) {
            currentState.handleRequest(this);
        }
    }
    
    public ElevatorState getCurrentState() {
        return currentState;
    }
}
