package com.rcpky.patterns.observer;

// Concrete observer - SMS Notification
public class SMSNotifier implements ParkingObserver {
    @Override
    public void onSpotTaken(int spotId) {
        System.out.println("SMS: Parking spot #" + spotId + " has been taken");
    }

    @Override
    public void onSpotFreed(int spotId) {
        System.out.println("SMS: Parking spot #" + spotId + " is now available");
    }
}