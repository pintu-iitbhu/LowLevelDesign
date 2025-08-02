package com.rcpky;

import com.rcpky.display.DisplayBoard;
import com.rcpky.models.ParkingRate;
import com.rcpky.spots.*;
import com.rcpky.tickets.ParkingTicket;
import com.rcpky.vehicles.Car;
import com.rcpky.vehicles.Motorcycle;
import com.rcpky.vehicles.Truck;
import com.rcpky.vehicles.Vehicle;

import java.util.*;

public class ParkingLot {
    private static ParkingLot instance = null;
    public ParkingRate rate = new ParkingRate();
    private Map<Integer, ParkingSpot> spots = new LinkedHashMap<>();
    private Map<Integer, ParkingTicket> tickets = new HashMap<>();
    private List<DisplayBoard> boards = new ArrayList<>();

    public ParkingLot() {}

    public static ParkingLot getInstance() {
        if (instance == null) instance = new ParkingLot();
        return instance;
    }


    public void addSpot(ParkingSpot s) { spots.put(s.getId(), s); }
    public void addDisplayBoard(DisplayBoard b) { boards.add(b); }
    public ParkingSpot getSpot(int id) { return spots.get(id); }
    public void freeSlot(int id) {
        ParkingSpot s = spots.get(id);
        if (s != null) s.removeVehicle();
    }

    public Collection<ParkingSpot> getAllSpots() { return spots.values(); }
    public ParkingTicket parkVehicle(Vehicle v) {
        for (ParkingSpot s : spots.values()) {
            if (s.isFree() && canFit(v, s)) {
                s.assignVehicle(v);
                ParkingTicket t = new ParkingTicket(s.getId(), v);
                tickets.put(t.getTicketNo(), t);
                return t;
            }
        }
        System.out.println("Sorry, parking lot is full. New cars cannot be parked.");
        return null;
    }

    private boolean canFit(Vehicle v, ParkingSpot s) {
        if (v instanceof Motorcycle && s instanceof MotorcycleSpot) return true;
        if ((v instanceof Truck || v instanceof Truck) && s instanceof Large) return true;
        if (v instanceof Car && (s instanceof Compact || s instanceof Handicapped)) return true;
        return false;
    }
}
