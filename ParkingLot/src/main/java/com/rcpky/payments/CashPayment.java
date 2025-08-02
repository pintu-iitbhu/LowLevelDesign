package com.rcpky.payments;

import com.rcpky.enums.PaymentStatus;
import com.rcpky.tickets.ParkingTicket;

import java.util.Date;

/**
 * Concrete implementation of PaymentStrategy for cash payments
 */
public class CashPayment extends Payment implements PaymentStrategy {
    public CashPayment() {
        super(0.0); // Initialize with zero amount, will be set during processing
        this.timestamp = new Date();
    }

    @Override
    public boolean processPayment(ParkingTicket ticket, double amount) {
        System.out.println("Processing cash payment of $" + amount);
        this.amount = amount;
        this.status = PaymentStatus.COMPLETED;
        ticket.setPayment(this);
        return true;
    }
    
    @Override
    public boolean initiateTransaction() {
        // For cash payments, transaction is always successful once initiated
        this.status = PaymentStatus.COMPLETED;
        System.out.println("Completed cash payment of $" + this.amount);
        return true;
    }
}
