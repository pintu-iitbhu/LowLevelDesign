package com.rcpky.accounts;

public abstract class Account {
    protected String username;
    protected String password;
    protected String phoneNumber;

    public Account(String username, String password, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public abstract boolean resetPassword(String newPassword);
}
