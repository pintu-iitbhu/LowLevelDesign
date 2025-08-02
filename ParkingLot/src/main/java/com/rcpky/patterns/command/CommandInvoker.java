package com.rcpky.patterns.command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Command Invoker that executes commands and maintains history for undo operations
 */
public class CommandInvoker {
    private final Deque<ParkingCommand> commandHistory = new ArrayDeque<>();
    private final int maxHistorySize;
    
    public CommandInvoker() {
        this(10); // Default history size
    }
    
    public CommandInvoker(int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
    }
    
    /**
     * Execute a command and add it to history if successful
     */
    public boolean executeCommand(ParkingCommand command) {
        boolean result = command.execute();
        
        if (result) {
            // Add to history for potential undo
            commandHistory.push(command);
            
            // Maintain history size limit
            if (commandHistory.size() > maxHistorySize) {
                commandHistory.removeLast();
            }
        }
        
        return result;
    }
    
    /**
     * Undo the most recent command
     */
    public boolean undoLastCommand() {
        if (commandHistory.isEmpty()) {
            System.out.println("No commands to undo");
            return false;
        }
        
        ParkingCommand lastCommand = commandHistory.pop();
        return lastCommand.undo();
    }
    
    /**
     * Get the number of commands in history
     */
    public int getHistorySize() {
        return commandHistory.size();
    }
    
    /**
     * Clear command history
     */
    public void clearHistory() {
        commandHistory.clear();
    }
}
