package com.example.bit_x.models;

import com.google.firebase.database.Exclude;

public class TraderModel {

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    String key;
    String name;
    String phoneNumber;
    double balance;

    public TraderModel() {
    }

    public TraderModel(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "Trader{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", balance=" + balance +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public TraderModel(String name, String phoneNumber, double balance) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }
}
