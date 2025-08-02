package com.rcpky.patterns;

import com.rcpky.display.DisplayBoard;
import com.rcpky.models.ParkingRate;
import com.rcpky.patterns.observer.ParkingObserver;
import com.rcpky.spots.ParkingSpot;
import com.rcpky.spots.VehicleAccommodator;
import com.rcpky.tickets.ParkingTicket;
import com.rcpky.vehicles.Vehicle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe implementation of ParkingLot using double-checked locking
 */
public class ThreadSafeParkingLot {
    // Volatile ensures visibility of changes across threads
    private static volatile ThreadSafeParkingLot instance;
    public ParkingRate rate = new ParkingRate();
    
    // Thread-safe collections
    private final Map<Integer, ParkingSpot> spots = new ConcurrentHashMap<>();
    private final Map<Integer, ParkingTicket> tickets = new ConcurrentHashMap<>();
    private final List<DisplayBoard> boards = Collections.synchronizedList(new ArrayList<>());
    
    // Observer pattern integration
    private final ParkingEventManager eventManager = new ParkingEventManager();
    
    // Read-write lock for optimizing concurrent access
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private ThreadSafeParkingLot() {
        // Private constructor to prevent instantiation
    }

    public static ThreadSafeParkingLot getInstance() {
        // Double-checked locking pattern
        if (instance == null) {
            synchronized (ThreadSafeParkingLot.class) {
                if (instance == null) {
                    instance = new ThreadSafeParkingLot();
                }
            }
        }
        return instance;
    }

    public void addObserver(ParkingObserver observer) {
        eventManager.subscribe(observer);
    }

    public void removeObserver(ParkingObserver observer) {
        eventManager.unsubscribe(observer);
    }

    public void addSpot(ParkingSpot s) {
        spots.put(s.getId(), s);
    }
    
    public void addDisplayBoard(DisplayBoard b) {
        boards.add(b);
    }
    
    public ParkingSpot getSpot(int id) {
        return spots.get(id);
    }
    
    public void freeSlot(int id) {
        try {
            lock.writeLock().lock();
            ParkingSpot s = spots.get(id);
            if (s != null && s.removeVehicle()) {
                // Notify observers about the freed spot
                eventManager.notifySpotFreed(id);
                // Update all display boards
                updateDisplays();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Collection<ParkingSpot> getAllSpots() {
        try {
            lock.readLock().lock();
            return new ArrayList<>(spots.values());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public ParkingTicket parkVehicle(Vehicle v) throws ParkingException {
        try {
            lock.writeLock().lock();
            for (ParkingSpot s : spots.values()) {
                if (s.isFree() && canFit(v, s)) {
                    s.assignVehicle(v);
                    ParkingTicket t = new ParkingTicket(s.getId(), v);
                    tickets.put(t.getTicketNo(), t);
                    
                    // Notify observers about the taken spot
                    eventManager.notifySpotTaken(s.getId());
                    // Update all display boards
                    updateDisplays();
                    
                    return t;
                }
            }
            throw new ParkingException("Parking lot is full. No available spots for this vehicle type.");
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void updateDisplays() {
        Collection<ParkingSpot> allSpots = getAllSpots();
        for (DisplayBoard board : boards) {
            board.update(allSpots);
        }
    }

    private boolean canFit(Vehicle v, ParkingSpot s) {
        // Use the VehicleAccommodator interface if the spot implements it
        if (s instanceof VehicleAccommodator) {
            return ((VehicleAccommodator) s).canAccommodateVehicle(v);
        }
        
        // Fallback to legacy type checking if needed
        String vehicleType = v.getClass().getSimpleName().toLowerCase();
        String spotType = s.getClass().getSimpleName().toLowerCase();
        
        if (vehicleType.contains("motorcycle") && spotType.contains("motorcycle")) return true;
        if ((vehicleType.contains("truck") || vehicleType.contains("van")) && spotType.contains("large")) return true;
        if (vehicleType.contains("car") && (spotType.contains("compact") || spotType.contains("handicapped"))) return true;
        
        return false;
    }
    
    /**
     * Custom exception for parking-related errors
     */
    public static class ParkingException extends Exception {
        public ParkingException(String message) {
            super(message);
        }
    }
}
