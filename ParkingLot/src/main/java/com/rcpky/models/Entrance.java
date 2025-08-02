package com.rcpky.models;

import com.rcpky.ParkingLot;
import com.rcpky.tickets.ParkingTicket;
import com.rcpky.vehicles.Vehicle;

public class Entrance {
    private int id;
    public Entrance(int id) { this.id = id; }
    public ParkingTicket getTicket(Vehicle v) {
        return ParkingLot.getInstance().parkVehicle(v);
    }
}
