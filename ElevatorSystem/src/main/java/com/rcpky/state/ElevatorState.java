package com.rcpky.state;

/**
 * Interface defining the state pattern for elevator state management
 */
public interface ElevatorState {
    /**
     * Process the elevator's request in the current state
     * @param context The elevator context
     */
    void handleRequest(ElevatorContext context);
}
