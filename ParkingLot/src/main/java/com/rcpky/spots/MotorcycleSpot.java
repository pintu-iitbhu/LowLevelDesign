package com.rcpky.spots;

import com.rcpky.vehicles.Motorcycle;
import com.rcpky.vehicles.Vehicle;

public class MotorcycleSpot extends ParkingSpot implements VehicleAccommodator {
    public MotorcycleSpot(int id) { super(id); }
    
    @Override
    public boolean assignVehicle(Vehicle v) {
        if (!canAccommodateVehicle(v)) return false;
        this.vehicle = v;
        this.isFree = false;
        System.out.println("Allocated Motorcycle slot " + id + " to " + v.getLicenseNo());
        return true;
    }
    
    @Override
    public boolean canAccommodateVehicle(Vehicle vehicle) {
        return vehicle instanceof Motorcycle;
    }
}
