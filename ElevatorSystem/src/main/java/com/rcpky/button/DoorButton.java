package com.rcpky.button;

import com.rcpky.enums.Direction;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Button to control elevator door opening and closing
 */
@Getter
@Log4j2
public class DoorButton implements Button {
    private final Direction buttonType; // UP for open, DOWN for close
    private boolean isPressed;
    private boolean isLit;

    public DoorButton(Direction buttonType) {
        this.buttonType = buttonType;
        this.isPressed = false;
        this.isLit = false;
    }

    @Override
    public void press() {
        this.isPressed = true;
        this.isLit = true;
        String action = buttonType == Direction.UP ? "open" : "close";
        log.info("Door {} button pressed", action);
    }

    @Override
    public void pressAndHold() {
        press();
        String action = buttonType == Direction.UP ? "open" : "close";
        log.info("Door {} button held", action);
    }

    @Override
    public void release() {
        this.isPressed = false;
        this.isLit = false;
        String action = buttonType == Direction.UP ? "open" : "close";
        log.info("Door {} button released", action);
    }

    @Override
    public boolean isPressed() {
        return isPressed;
    }
}
