package com.rcpky.patterns.observer;

// Observer interface
public interface ParkingObserver {
    void onSpotTaken(int spotId);
    void onSpotFreed(int spotId);
}
