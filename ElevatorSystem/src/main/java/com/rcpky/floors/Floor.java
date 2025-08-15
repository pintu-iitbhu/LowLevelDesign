package com.rcpky.floors;

import com.rcpky.display.Display;
import com.rcpky.enums.Direction;
import com.rcpky.panels.HallPanel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Represents a floor in the building with hall panel and display
 */
@Getter
@Log4j2
public class Floor {
    private final int floorNumber;
    private final HallPanel hallPanel;
    private final Display display;
    
    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.hallPanel = new HallPanel(floorNumber);
        this.display = new Display();
        log.info("Floor {} initialized", floorNumber);
    }
    
    /**
     * Update the display to show elevator information
     * 
     * @param elevatorId ID of the elevator
     * @param direction Direction of the elevator
     */
    public void updateDisplay(int elevatorId, Direction direction) {
        display.showFloor(floorNumber);
        display.showDirection(direction);
        display.showElevatorId(elevatorId);
        log.info("Floor {} display updated for elevator {}", floorNumber, elevatorId);
    }
    
    /**
     * Gets the hall panel for this floor
     *
     * @return HallPanel for this floor
     */
    public HallPanel getHallPanel() {
        return hallPanel;
    }
}