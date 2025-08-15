package com.rcpky.observer;

/**
 * Observer interface for elevator events
 */
public interface ElevatorObserver {
    /**
     * Called when an elevator arrives at a floor
     * @param elevatorId The ID of the elevator
     * @param floorNumber The floor where the elevator arrived
     */
    void onElevatorArrived(int elevatorId, int floorNumber);
    
    /**
     * Called when elevator door opens
     * @param elevatorId The ID of the elevator
     * @param floorNumber The floor where the elevator is
     */
    void onDoorOpened(int elevatorId, int floorNumber);
    
    /**
     * Called when elevator door closes
     * @param elevatorId The ID of the elevator
     * @param floorNumber The floor where the elevator is
     */
    void onDoorClosed(int elevatorId, int floorNumber);
    
    /**
     * Called when an emergency is triggered in the elevator
     * @param elevatorId The ID of the elevator
     * @param floorNumber The current floor of the elevator
     */
    void onEmergency(int elevatorId, int floorNumber);
}
