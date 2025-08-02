package com.rcpky.payments;

import com.rcpky.enums.PaymentStatus;
import com.rcpky.tickets.ParkingTicket;

import java.util.Date;

/**
 * Concrete implementation of PaymentStrategy for credit card payments
 */
public class CreditCardPayment extends Payment implements PaymentStrategy {
    private String cardNumber;
    private String name;
    private String cvv;
    private String expiryDate;

    public CreditCardPayment(String cardNumber, String name, String cvv, String expiryDate) {
        super(0.0); // Initialize with zero amount, will be set during processing
        this.cardNumber = cardNumber;
        this.name = name;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.timestamp = new Date();
    }

    @Override
    public boolean processPayment(ParkingTicket ticket, double amount) {
        // In a real implementation, this would connect to a payment gateway
        boolean paymentSuccessful = validateCard() && processTransaction(amount);
        
        if (paymentSuccessful) {
            System.out.println("Processing credit card payment of $" + amount);
            this.amount = amount;
            this.status = PaymentStatus.COMPLETED;
            ticket.setPayment(this);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean initiateTransaction() {
        // This implementation is used when calling directly as a Payment
        return validateCard() && processTransaction(this.amount);
    }
    
    private boolean validateCard() {
        // Card validation logic
        return cardNumber != null && cardNumber.length() >= 16;
    }
    
    private boolean processTransaction(double amount) {
        // Transaction processing logic
        System.out.println("Processing $" + amount + " transaction for card ending with " 
            + cardNumber.substring(cardNumber.length() - 4));
        return true;
    }
}
