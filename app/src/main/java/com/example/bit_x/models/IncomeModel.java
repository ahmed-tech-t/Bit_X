package com.example.bit_x.models;

import com.google.firebase.database.Exclude;

public class IncomeModel {
    public IncomeModel(String name, double balance, String date) {
        this.name = name;
        this.balance = balance;
        this.date = date;
    }

    public IncomeModel(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public IncomeModel() {
    }

    @Override
    public String toString() {
        return "IncomeModel{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", date='" + date + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    String key;
    String name;
    double balance;
    String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
