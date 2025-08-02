package com.rcpky.patterns.observer;

// Concrete observer - Display Board Notification
public class DisplayNotifier implements ParkingObserver {
    @Override
    public void onSpotTaken(int spotId) {
        System.out.println("Display: Updated to show spot #" + spotId + " as occupied");
    }

    @Override
    public void onSpotFreed(int spotId) {
        System.out.println("Display: Updated to show spot #" + spotId + " as available");
    }
}
