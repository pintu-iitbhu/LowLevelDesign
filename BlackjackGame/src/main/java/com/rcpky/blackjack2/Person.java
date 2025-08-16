package com.rcpky.blackjack2;

import java.util.*;

public class Person {
    private String name;
    private String streetAddress;
    private String city;
    private String state;
    private String country;

    public Person() {}

    public Person(String name, String streetAddress, String city, String state, String country) {
        this.name = name;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    // Getters & setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
