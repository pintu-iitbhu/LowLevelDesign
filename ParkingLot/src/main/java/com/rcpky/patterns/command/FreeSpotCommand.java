package com.rcpky.patterns.command;

import com.rcpky.patterns.ThreadSafeParkingLot;
import com.rcpky.spots.ParkingSpot;
import com.rcpky.vehicles.Vehicle;

/**
 * Concrete command for freeing a parking spot
 */
public class FreeSpotCommand implements ParkingCommand {
    private final ThreadSafeParkingLot parkingLot;
    private final int spotId;
    private Vehicle vehicle; // To store vehicle for undo operation
    private boolean wasSpotOccupied;

    public FreeSpotCommand(ThreadSafeParkingLot parkingLot, int spotId) {
        this.parkingLot = parkingLot;
        this.spotId = spotId;
    }

    @Override
    public boolean execute() {
        ParkingSpot spot = parkingLot.getSpot(spotId);
        if (spot == null) {
            System.err.println("Invalid spot ID: " + spotId);
            return false;
        }
        
        wasSpotOccupied = !spot.isFree();
        if (wasSpotOccupied) {
            // Save the vehicle for potential undo
            vehicle = spot.getVehicle();
            // Free the spot
            parkingLot.freeSlot(spotId);
            return true;
        } else {
            System.err.println("Spot " + spotId + " is already free");
            return false;
        }
    }

    @Override
    public boolean undo() {
        // To undo freeing a spot, we need to re-assign the vehicle
        if (wasSpotOccupied && vehicle != null) {
            ParkingSpot spot = parkingLot.getSpot(spotId);
            if (spot != null && spot.isFree()) {
                return spot.assignVehicle(vehicle);
            }
        }
        return false;
    }
}
