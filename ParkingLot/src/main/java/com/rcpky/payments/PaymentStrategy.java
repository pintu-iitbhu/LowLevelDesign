package com.rcpky.payments;

import com.rcpky.tickets.ParkingTicket;

/**
 * Strategy Pattern: Interface for different payment methods
 */
public interface PaymentStrategy {
    boolean processPayment(ParkingTicket ticket, double amount);
}
