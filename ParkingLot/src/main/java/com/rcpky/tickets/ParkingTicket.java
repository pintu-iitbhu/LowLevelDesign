package com.rcpky.tickets;

import com.rcpky.enums.PaymentStatus;
import com.rcpky.enums.TicketStatus;
import com.rcpky.models.Entrance;
import com.rcpky.models.Exit;
import com.rcpky.payments.Payment;
import com.rcpky.vehicles.Vehicle;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

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
