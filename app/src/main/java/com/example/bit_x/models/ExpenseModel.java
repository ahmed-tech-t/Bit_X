package com.example.bit_x.models;

import com.google.firebase.database.Exclude;

public class ExpenseModel {


    @Exclude
    String key;
    TraderModel traderModel;
    String description;
    double value;

    public ExpenseModel() {
    }

    double payed;

    public ExpenseModel(TraderModel traderModel, String description, double value, double payed) {
        this.traderModel = traderModel;
        this.description = description;
        this.value = value;
        this.payed = payed;
        this.debts = payed-value;
    }

    double debts;


    public TraderModel getTraderModel() {
        return traderModel;
    }

    public void setTraderModel(TraderModel traderModel) {
        this.traderModel = traderModel;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ExpenseModel{" +
                "traderName='" + traderModel + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", payed=" + payed +
                ", debts=" + debts +
                '}';
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getPayed() {
        return payed;
    }

    public void setPayed(double payed) {
        this.payed = payed;
    }

    public double getDebts() {
        return debts;
    }

    public void setDebts(double debts) {
        this.debts = debts;
    }


}
