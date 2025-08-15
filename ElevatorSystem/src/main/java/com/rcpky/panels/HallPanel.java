package com.rcpky.panels;

import com.rcpky.button.HallButton;
import com.rcpky.enums.Direction;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Panel at each floor to call the elevator with up and down buttons
 */
@Getter
@Log4j2
public class HallPanel {
    private final HallButton upButton;
    private final HallButton downButton;
    private final int floorNumber;
    
    public HallPanel(int floorNumber) {
        this.floorNumber = floorNumber;
        this.upButton = new HallButton(floorNumber, Direction.IDLE);
        this.downButton = new HallButton(floorNumber, Direction.IDLE);
        log.info("Hall panel initialized for floor {}", floorNumber);
    }
    
    // Default constructor for the ground floor panel
    public HallPanel() {
        this(0); // Default to ground floor
    }
    
    public void pressUpButton() {
        upButton.press();
        log.info("Up button pressed on floor {}", floorNumber);
    }
    
    public void pressDownButton() {
        downButton.press();
        log.info("Down button pressed on floor {}", floorNumber);
    }
    
    public void resetUpButton() {
        upButton.turnLightOff();
        log.info("Up button light turned off on floor {}", floorNumber);
    }
    
    public void resetDownButton() {
        downButton.turnLightOff();
        log.info("Down button light turned off on floor {}", floorNumber);
    }
}
