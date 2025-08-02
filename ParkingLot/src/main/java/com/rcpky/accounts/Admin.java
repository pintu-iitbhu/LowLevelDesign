package com.rcpky.accounts;

public class Admin extends Account{
    public Admin(String username, String password, String phoneNumber) {
        super(username, password, phoneNumber);
    }

    @Override
    public boolean resetPassword(String newPassword) {
        this.password = newPassword;
        return true;
    }
}
