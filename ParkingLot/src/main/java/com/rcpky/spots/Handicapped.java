package com.rcpky.spots;

import com.rcpky.vehicles.Vehicle;

public class Handicapped extends ParkingSpot{

    public Handicapped(int id) { super(id); }
    public boolean assignVehicle(Vehicle v) {
        if (isFree) {
            System.out.println("Allocated Handicapped slot " + id + " to " + v.getLicenseNo());
            this.vehicle = v; isFree = false; return true;
        }
        return false;
    }
}


