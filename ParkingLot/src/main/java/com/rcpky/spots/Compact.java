package com.rcpky.spots;

import com.rcpky.vehicles.Car;
import com.rcpky.vehicles.Vehicle;

public class Compact extends ParkingSpot implements VehicleAccommodator {
    public Compact(int id) { super(id); }
    
    @Override
    public boolean assignVehicle(Vehicle v) {
        if (!canAccommodateVehicle(v)) return false;
        this.vehicle = v;
        this.isFree = false;
        System.out.println("Allocated Compact slot " + id + " to " + v.getLicenseNo());
        return true;
    }
    
    @Override
    public boolean canAccommodateVehicle(Vehicle vehicle) {
        return vehicle instanceof Car;
    }
}
