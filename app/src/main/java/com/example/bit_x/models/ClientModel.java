package com.example.bit_x.models;

import com.google.firebase.database.Exclude;

public class ClientModel {
    @Exclude
    String key;
    String name;
    String phoneNumber;
    double balance;

    public ClientModel() {
    }

    public ClientModel(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }


    public ClientModel( String name, String phoneNumber, double balance) {
          this.name = name;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "ClientModel{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", balance=" + balance +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
}
