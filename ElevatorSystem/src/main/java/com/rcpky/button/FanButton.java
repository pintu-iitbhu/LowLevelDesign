package com.rcpky.button;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Button to control the fan inside the elevator
 */
@Getter
@Log4j2
public class FanButton implements Button {
    private boolean isPressed;
    private boolean isLit;
    private boolean isFanOn;

    public FanButton() {
        this.isPressed = false;
        this.isLit = false;
        this.isFanOn = false;
    }

    @Override
    public void press() {
        this.isPressed = true;
        this.isFanOn = !this.isFanOn;
        this.isLit = this.isFanOn;
        
        if (isFanOn) {
            log.info("Fan button pressed. Fan turned ON.");
        } else {
            log.info("Fan button pressed. Fan turned OFF.");
        }
    }

    @Override
    public void pressAndHold() {
        press();
        log.info("Fan button held. No additional effect.");
    }

    @Override
    public void release() {
        this.isPressed = false;
        // Fan state doesn't change on release
        log.info("Fan button released.");
    }

    @Override
    public boolean isPressed() {
        return isPressed;
    }
    
    public void turnOff() {
        if (isFanOn) {
            this.isFanOn = false;
            this.isLit = false;
            log.info("Fan turned OFF.");
        }
    }
}
