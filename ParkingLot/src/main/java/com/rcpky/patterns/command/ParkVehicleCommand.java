package com.rcpky.patterns.command;

import com.rcpky.patterns.ThreadSafeParkingLot;
import com.rcpky.patterns.ThreadSafeParkingLot.ParkingException;
import com.rcpky.tickets.ParkingTicket;
import com.rcpky.vehicles.Vehicle;

/**
 * Concrete command for parking a vehicle
 */
public class ParkVehicleCommand implements ParkingCommand {
    private final ThreadSafeParkingLot parkingLot;
    private final Vehicle vehicle;
    private ParkingTicket ticket;
    private int spotId;

    public ParkVehicleCommand(ThreadSafeParkingLot parkingLot, Vehicle vehicle) {
        this.parkingLot = parkingLot;
        this.vehicle = vehicle;
    }

    @Override
    public boolean execute() {
        try {
            ticket = parkingLot.parkVehicle(vehicle);
            spotId = ticket.getSlotNo();
            return true;
        } catch (ParkingException e) {
            System.err.println("Failed to park vehicle: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean undo() {
        // To undo parking, we free the spot
        if (ticket != null) {
            parkingLot.freeSlot(spotId);
            return true;
        }
        return false;
    }
    
    /**
     * Get the ticket generated from parking
     */
    public ParkingTicket getTicket() {
        return ticket;
    }
}
