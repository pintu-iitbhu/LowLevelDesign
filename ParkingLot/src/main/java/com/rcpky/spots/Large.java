package com.rcpky.spots;

import com.rcpky.vehicles.Truck;
import com.rcpky.vehicles.Van;
import com.rcpky.vehicles.Vehicle;

public class Large extends ParkingSpot implements VehicleAccommodator {
    public Large(int id) { super(id); }
    
    @Override
    public boolean assignVehicle(Vehicle v) {
        if (!canAccommodateVehicle(v)) return false;
        this.vehicle = v;
        this.isFree = false;
        System.out.println("Allocated Large slot " + id + " to " + v.getLicenseNo());
        return true;
    }
    
    @Override
    public boolean canAccommodateVehicle(Vehicle vehicle) {
        return vehicle instanceof Truck || vehicle instanceof Van;
    }
}
