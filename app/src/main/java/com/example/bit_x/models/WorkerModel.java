package com.example.bit_x.models;

import com.google.firebase.database.Exclude;

public class WorkerModel {
    @Exclude
    String key;
    String name;
    String phoneNumber;
    double balance;

    public WorkerModel(String key, String name, String phoneNumber, double balance) {
        this.key = key;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    public WorkerModel(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public WorkerModel() {
    }


    @Override
    public String toString() {
        return "WorkerModel{" +
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

    public WorkerModel(String name, String phoneNumber, double balance) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }
}
