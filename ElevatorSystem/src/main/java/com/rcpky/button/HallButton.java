package com.rcpky.button;

import com.rcpky.enums.Direction;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Button in hall panels used to request an elevator to go up or down
 */
@Getter
@Log4j2
public class HallButton implements Button {
    private final int floor;
    private final Direction direction;
    private boolean isPressed;
    private boolean isLit;
    
    public HallButton(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
        this.isPressed = false;
        this.isLit = false;
    }
    
    @Override
    public void press() {
        this.isPressed = true;
        this.isLit = true;
        log.info("Hall button pressed for {} direction on floor {}", direction, floor);
    }
    
    @Override
    public void pressAndHold() {
        press();
        log.info("Hall button held for {} direction on floor {}", direction, floor);
    }
    
    @Override
    public void release() {
        this.isPressed = false;
        // Light stays on until elevator arrives
        log.info("Hall button released for {} direction on floor {}", direction, floor);
    }
    
    @Override
    public boolean isPressed() {
        return isPressed;
    }
    
    @Override
    public void turnLightOn() {
        this.isLit = true;
        log.info("Hall button light turned ON for {} direction on floor {}", direction, floor);
    }
    
    @Override
    public void turnLightOff() {
        this.isLit = false;
        log.info("Hall button light turned OFF for {} direction on floor {}", direction, floor);
    }
}
