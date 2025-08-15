package com.rcpky.button;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Emergency button to stop the elevator in case of emergency
 */
@Getter
@Log4j2
public class EmergencyButton implements Button {
    private boolean isPressed;
    private boolean isLit;
    private boolean isAlarmOn;

    public EmergencyButton() {
        this.isPressed = false;
        this.isLit = false;
        this.isAlarmOn = false;
    }

    @Override
    public void press() {
        this.isPressed = true;
        this.isLit = true;
        this.isAlarmOn = true;
        log.warn("EMERGENCY button pressed! Alarm activated.");
    }

    @Override
    public void pressAndHold() {
        press();
        log.warn("EMERGENCY button held! Continuing alarm.");
    }

    @Override
    public void release() {
        this.isPressed = false;
        // Light and alarm stay on until reset
        log.info("EMERGENCY button released, but alarm remains active.");
    }

    @Override
    public boolean isPressed() {
        return isPressed;
    }
    
    public void reset() {
        this.isLit = false;
        this.isAlarmOn = false;
        log.info("EMERGENCY button has been reset.");
    }
}
