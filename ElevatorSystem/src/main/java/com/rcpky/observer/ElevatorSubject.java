package com.rcpky.observer;

/**
 * Subject interface for the Observer pattern
 */
public interface ElevatorSubject {
    /**
     * Register an observer to receive notifications
     * @param observer The observer to register
     */
    void addObserver(ElevatorObserver observer);
    
    /**
     * Unregister an observer from notifications
     * @param observer The observer to unregister
     */
    void removeObserver(ElevatorObserver observer);
    
    /**
     * Notify all observers when elevator arrives at a floor
     * @param elevatorId The ID of the elevator
     * @param floorNumber The floor number
     */
    void notifyElevatorArrival(int elevatorId, int floorNumber);
    
    /**
     * Notify all observers when elevator door opens
     * @param elevatorId The ID of the elevator
     * @param floorNumber The floor number
     */
    void notifyDoorOpened(int elevatorId, int floorNumber);
    
    /**
     * Notify all observers when elevator door closes
     * @param elevatorId The ID of the elevator
     * @param floorNumber The floor number
     */
    void notifyDoorClosed(int elevatorId, int floorNumber);
    
    /**
     * Notify all observers of an emergency situation
     * @param elevatorId The ID of the elevator
     * @param floorNumber The floor number
     */
    void notifyEmergency(int elevatorId, int floorNumber);
}
