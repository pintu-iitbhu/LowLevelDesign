package com.rcpky.display;

import com.rcpky.enums.Direction;
import com.rcpky.enums.ElevatorState;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Display panel showing elevator information
 */
@Getter
@Log4j2
public class Display {
    private int currentFloor;
    private Direction direction;
    private String message;
    private int elevatorId;
    private int currentLoad;
    private int maxCapacity;
    private ElevatorState elevatorState;
    private boolean isUnderMaintenance;

    public Display() {
        this.currentFloor = 0;
        this.direction = Direction.IDLE;
        this.message = "";
        this.elevatorId = -1;
        this.currentLoad = 0;
        this.maxCapacity = 0;
        this.elevatorState = ElevatorState.IDLE;
        this.isUnderMaintenance = false;
        log.info("Display initialized");
    }

    /**
     * Show current floor number
     * 
     * @param floor Floor number to display
     */
    public void showFloor(int floor) {
        this.currentFloor = floor;
        log.info("Display showing floor: {}", floor);
    }

    /**
     * Show elevator direction
     * 
     * @param direction Direction to display
     */
    public void showDirection(Direction direction) {
        this.direction = direction;
        log.info("Display showing direction: {}", direction);
    }

    /**
     * Show a text message on the display
     * 
     * @param message Message to display
     */
    public void showMessage(String message) {
        this.message = message;
        log.info("Display showing message: {}", message);
    }

    /**
     * Show elevator ID on the display
     * 
     * @param elevatorId ID of the elevator
     */
    public void showElevatorId(int elevatorId) {
        this.elevatorId = elevatorId;
        log.info("Display showing elevator ID: {}", elevatorId);
    }

    /**
     * Show current load and capacity information
     * 
     * @param currentLoad Current load in the elevator
     * @param maxCapacity Maximum capacity of the elevator
     */
    public void showLoadInfo(int currentLoad, int maxCapacity) {
        this.currentLoad = currentLoad;
        this.maxCapacity = maxCapacity;
        log.info("Display showing load: {}/{}", currentLoad, maxCapacity);
    }

    /**
     * Show elevator state
     * 
     * @param state Current state of the elevator
     */
    public void showElevatorState(ElevatorState state) {
        this.elevatorState = state;
        log.info("Display showing elevator state: {}", state);
    }

    /**
     * Show maintenance status
     * 
     * @param isUnderMaintenance Whether the elevator is under maintenance
     */
    public void showMaintenanceStatus(boolean isUnderMaintenance) {
        this.isUnderMaintenance = isUnderMaintenance;
        if (isUnderMaintenance) {
            showMessage("UNDER MAINTENANCE");
        } else {
            showMessage("IN SERVICE");
        }
    }
    
    /**
     * Get complete display information as a string
     * 
     * @return String representation of all display information
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Floor: ").append(currentFloor).append(" | ");
        
        if (elevatorId >= 0) {
            sb.append("Elevator: ").append(elevatorId).append(" | ");
        }
        
        String dirStr;
        switch (direction) {
            case UP:
                dirStr = "▲";
                break;
            case DOWN:
                dirStr = "▼";
                break;
            default:
                dirStr = "■";
        }
        sb.append("Direction: ").append(dirStr).append(" | ");
        
        if (maxCapacity > 0) {
            sb.append("Load: ").append(currentLoad).append("/").append(maxCapacity).append(" | ");
        }
        
        if (isUnderMaintenance) {
            sb.append("MAINTENANCE MODE");
        } else {
            sb.append("State: ").append(elevatorState);
        }
        
        if (!message.isEmpty()) {
            sb.append(" | ").append(message);
        }
        
        return sb.toString();
    }
}
