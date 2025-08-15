package com.rcpky.floors;

import com.rcpky.ElevatorSystem;
import com.rcpky.enums.Direction;
import com.rcpky.observer.ElevatorObserver;
import com.rcpky.panels.HallPanel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages floors and elevator requests from floors
 */
@Getter
@Log4j2
public class FloorManager implements ElevatorObserver {
    private final Map<Integer, Floor> floors;
    private final int totalFloors;
    private final ElevatorSystem elevatorSystem;

    public FloorManager(int totalFloors, ElevatorSystem elevatorSystem) {
        this.totalFloors = totalFloors;
        this.elevatorSystem = elevatorSystem;
        this.floors = new HashMap<>();
        initializeFloors();
        log.info("FloorManager initialized with {} floors", totalFloors);
    }

    private void initializeFloors() {
        for (int i = 0; i < totalFloors; i++) {
            floors.put(i, new Floor(i));
        }
        log.info("All {} floors initialized", totalFloors);
    }

    public void requestElevator(int floorNumber, Direction direction) {
        if (!isValidFloor(floorNumber)) {
            log.error("Invalid floor number: {}", floorNumber);
            return;
        }

        // Get the floor and light up the appropriate button
        Floor floor = floors.get(floorNumber);
        HallPanel hallPanel = floor.getHallPanel();
        
        if (direction == Direction.UP) {
            hallPanel.pressUpButton();
        } else if (direction == Direction.DOWN) {
            hallPanel.pressDownButton();
        }
        
        // Forward the request to the elevator system
        elevatorSystem.processExternalRequest(floorNumber, direction);
        log.info("Elevator requested from floor {} for {} direction", floorNumber, direction);
    }

    private boolean isValidFloor(int floorNumber) {
        return floorNumber >= 0 && floorNumber < totalFloors;
    }

    public Floor getFloor(int floorNumber) {
        if (!isValidFloor(floorNumber)) {
            log.error("Requested invalid floor: {}", floorNumber);
            return null;
        }
        return floors.get(floorNumber);
    }
    
    // Observer pattern implementation
    @Override
    public void onElevatorArrived(int elevatorId, int floorNumber) {
        if (!isValidFloor(floorNumber)) {
            return;
        }
        
        Floor floor = floors.get(floorNumber);
        HallPanel hallPanel = floor.getHallPanel();
        
        // Reset the hall buttons based on the elevator's arrival
        Direction elevatorDirection = elevatorSystem.getElevatorDirection(elevatorId);
        if (elevatorDirection == Direction.UP || elevatorDirection == Direction.IDLE) {
            hallPanel.resetUpButton();
        }
        
        if (elevatorDirection == Direction.DOWN || elevatorDirection == Direction.IDLE) {
            hallPanel.resetDownButton();
        }
        
        log.info("Floor {} notified of elevator {} arrival", floorNumber, elevatorId);
    }

    @Override
    public void onDoorOpened(int elevatorId, int floorNumber) {
        if (isValidFloor(floorNumber)) {
            log.info("Floor {} notified of elevator {} door opening", floorNumber, elevatorId);
            // Could update floor display status here
        }
    }

    @Override
    public void onDoorClosed(int elevatorId, int floorNumber) {
        if (isValidFloor(floorNumber)) {
            log.info("Floor {} notified of elevator {} door closing", floorNumber, elevatorId);
            // Could update floor display status here
        }
    }

    @Override
    public void onEmergency(int elevatorId, int floorNumber) {
        if (isValidFloor(floorNumber)) {
            log.warn("Floor {} notified of emergency in elevator {}", floorNumber, elevatorId);
            // Could trigger emergency protocols on the floor
        }
    }

    public void updateDisplay(int floorNumber, int id, Direction currentDirection, String elevatorAssigned) {
        Floor floor = floors.get(floorNumber);
        floor.updateDisplay(id, currentDirection);
    }
}
