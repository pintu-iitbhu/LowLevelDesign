package com.rcpky.patterns.command;

/**
 * Command Pattern: Interface for all parking operations
 * Allows operations to be queued, logged, and potentially undone
 */
public interface ParkingCommand {
    /**
     * Execute the command
     */
    boolean execute();
    
    /**
     * Undo the command if supported
     */
    boolean undo();
}
