package com.rcpky;

import com.rcpky.enums.DoorState;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public class Door {
    private DoorState state;
    private boolean sensorActivated;

    public Door() {
        this.state = DoorState.CLOSED;
        this.sensorActivated = false;
    }

    public void open() {
        if (state == DoorState.CLOSED || state == DoorState.CLOSING) {
            log.info("Opening door");
            this.state = DoorState.OPENING;
            
            // Simulate door opening time
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            this.state = DoorState.OPEN;
            log.info("Door is open");
        }
    }
    
    public void close() {
        if (state == DoorState.OPEN || state == DoorState.OPENING) {
            if (sensorActivated) {
                log.info("Cannot close door: obstacle detected");
                return;
            }
            
            log.info("Closing door");
            this.state = DoorState.CLOSING;
            
            // Simulate door closing time
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            this.state = DoorState.CLOSED;
            log.info("Door is closed");
        }
    }
    
    public void obstacleSensed() {
        this.sensorActivated = true;
        if (state == DoorState.CLOSING) {
            log.info("Obstacle detected! Reopening door");
            open();
        }
    }
    
    public void obstacleCleared() {
        this.sensorActivated = false;
        log.info("Obstacle cleared");
    }

    public boolean isOpen() {
        return this.state == DoorState.OPEN || this.state == DoorState.OPENING;
    }
    
    public boolean isClosed() {
        return this.state == DoorState.CLOSED;
    }
}
