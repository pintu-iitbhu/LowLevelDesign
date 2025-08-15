package com.rcpky.button;

/**
 * Interface for all buttons in the elevator system
 */
public interface Button {
    /**
     * Press the button once
     */
    void press();
    
    /**
     * Press and hold the button
     */
    void pressAndHold();
    
    /**
     * Release the button
     */
    void release();
    
    /**
     * Check if button is currently pressed
     * @return true if pressed, false otherwise
     */
    boolean isPressed();
    
    /**
     * Turn the button light on
     */
    default void turnLightOn() {
        // Default implementation can be overridden by concrete buttons
    }
    
    /**
     * Turn the button light off
     */
    default void turnLightOff() {
        // Default implementation can be overridden by concrete buttons
    }
}
