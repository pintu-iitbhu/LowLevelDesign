package com.rcpky.button;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Button inside the elevator to select destination floors
 */
@Getter
@Log4j2
public class ElevatorButton implements Button {
    private final int floorNumber;
    private boolean isPressed;
    private boolean isLit;
    
    public ElevatorButton(int floorNumber) {
        this.floorNumber = floorNumber;
        this.isPressed = false;
        this.isLit = false;
    }
    
    @Override
    public void press() {
        this.isPressed = true;
        this.isLit = true;
        log.info("Elevator button pressed for floor {}", floorNumber);
    }
    
    @Override
    public void pressAndHold() {
        press();
        log.info("Elevator button held for floor {}", floorNumber);
    }
    
    @Override
    public void release() {
        this.isPressed = false;
        // Light stays on until the floor is reached
        log.info("Elevator button released for floor {}", floorNumber);
    }
    
    @Override
    public boolean isPressed() {
        return isPressed;
    }
    
    @Override
    public void turnLightOn() {
        this.isLit = true;
        log.info("Elevator button light turned ON for floor {}", floorNumber);
    }
    
    @Override
    public void turnLightOff() {
        this.isLit = false;
        log.info("Elevator button light turned OFF for floor {}", floorNumber);
    }
}
