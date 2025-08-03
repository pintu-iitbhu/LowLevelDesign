package com.rcpky.tickets;

import com.rcpky.enums.TicketStatus;
import com.rcpky.payments.Payment;
import com.rcpky.vehicles.Vehicle;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
public class ParkingTicket {
    private static int ticketSeed = 1000;
    private int ticketNo;
    private int slotNo;
    private Vehicle vehicle;
    private Date entryTime, exitTime;
    private double amount;
    private TicketStatus status;
    private Payment payment;

    public ParkingTicket(int slotNo, Vehicle v) {
        this.ticketNo = ticketSeed++;
        this.slotNo = slotNo;
        this.vehicle = v;
        this.entryTime = new Date();
        this.status = TicketStatus.ISSUED;
        v.assignTicket(this);
        System.out.println("Ticket issued: " + ticketNo);
    }




}
