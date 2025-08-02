package com.rcpky.payments;

import com.rcpky.enums.PaymentStatus;

import java.util.Date;

public abstract class Payment {
    protected double amount;
    protected PaymentStatus status;
    protected Date timestamp;

    public Payment(double amt) { this.amount = amt; this.status = PaymentStatus.PENDING; this.timestamp = new Date(); }
    public abstract boolean initiateTransaction();

}
