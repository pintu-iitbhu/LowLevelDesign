package com.rcpky.spots;

import com.rcpky.vehicles.Vehicle;

/**
 * Interface for spot-vehicle compatibility checks
 * Adheres to Interface Segregation Principle (ISP)
 */
public interface VehicleAccommodator {
    /**
     * Check if this spot can accommodate the given vehicle
     * @param vehicle The vehicle to check
     * @return true if the vehicle can be parked in this spot
     */
    boolean canAccommodateVehicle(Vehicle vehicle);
}
