package com.rcpky.panels;

import com.rcpky.button.DoorButton;
import com.rcpky.button.ElevatorButton;
import com.rcpky.button.EmergencyButton;
import com.rcpky.button.FanButton;
import com.rcpky.enums.Direction;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * Panel inside the elevator with floor buttons and control buttons
 */
@Getter
@Log4j2
public class ElevatorPanel {
    private final Map<Integer, ElevatorButton> floorButtons;
    private final EmergencyButton emergencyButton;
    private final DoorButton openDoorButton;
    private final DoorButton closeDoorButton;
    private final FanButton fanButton;
    
    public ElevatorPanel() {
        this.floorButtons = new HashMap<>();
        this.emergencyButton = new EmergencyButton();
        this.openDoorButton = new DoorButton(Direction.UP); // UP for open
        this.closeDoorButton = new DoorButton(Direction.DOWN); // DOWN for close
        this.fanButton = new FanButton();
        log.info("Elevator panel initialized");
    }
    
    public void initializeFloorButtons(int maxFloors) {
        for (int i = 0; i < maxFloors; i++) {
            floorButtons.put(i, new ElevatorButton(i));
        }
        log.info("Initialized {} floor buttons", maxFloors);
    }
    
    public ElevatorButton getFloorButton(int floor) {
        return floorButtons.get(floor);
    }
    
    public void pressFloorButton(int floor) {
        ElevatorButton button = floorButtons.get(floor);
        if (button != null) {
            button.press();
            log.info("Floor {} button pressed on elevator panel", floor);
        } else {
            log.error("No button for floor {} exists", floor);
        }
    }
    
    public void resetFloorButton(int floor) {
        ElevatorButton button = floorButtons.get(floor);
        if (button != null) {
            button.turnLightOff();
            log.info("Floor {} button light turned off", floor);
        }
    }
    
    public void pressEmergencyButton() {
        emergencyButton.press();
    }
    
    public void pressOpenDoorButton() {
        openDoorButton.press();
    }
    
    public void pressCloseDoorButton() {
        closeDoorButton.press();
    }
    
    public void pressFanButton() {
        fanButton.press();
    }
}
