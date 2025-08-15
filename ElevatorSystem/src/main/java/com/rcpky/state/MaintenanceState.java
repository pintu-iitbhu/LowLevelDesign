package com.rcpky.state;

import lombok.extern.log4j.Log4j2;

/**
 * Represents the maintenance state of an elevator
 */
@Log4j2
public class MaintenanceState implements ElevatorState {
    @Override
    public void handleRequest(ElevatorContext context) {
        // In maintenance state, the elevator should not respond to regular requests
        log.info("Elevator is in maintenance mode. All regular operations suspended.");
        
        // This could notify the system administrator or maintenance team
    }
}
