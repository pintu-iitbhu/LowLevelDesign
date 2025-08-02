package com.rcpky.patterns;

import com.rcpky.patterns.observer.ParkingObserver;

import java.util.ArrayList;
import java.util.List;

// Subject class that maintains a list of observers
public class ParkingEventManager {
    private List<ParkingObserver> observers = new ArrayList<>();

    public ParkingEventManager() {}

    public void subscribe(ParkingObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(ParkingObserver observer) {
        observers.remove(observer);
    }

    public void notifySpotTaken(int spotId) {
        for (ParkingObserver observer : observers) {
            observer.onSpotTaken(spotId);
        }
    }

    public void notifySpotFreed(int spotId) {
        for (ParkingObserver observer : observers) {
            observer.onSpotFreed(spotId);
        }
    }
}
