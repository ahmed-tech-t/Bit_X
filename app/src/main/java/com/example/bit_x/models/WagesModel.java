package com.example.bit_x.models;

import com.google.firebase.database.Exclude;

public class WagesModel {
    @Exclude
    String key;
    WorkerModel workerModel;
    String description;
    double value;
    double payed;
    double debts;

    public WagesModel() {
    }

    public WagesModel(WorkerModel workerModel, String description, double value, double payed) {
        this.workerModel = workerModel;
        this.description = description;
        this.value = value;
        this.payed = payed;
        this.debts = payed-value;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public WorkerModel getWorkerModel() {
        return workerModel;
    }

    public void setWorkerModel(WorkerModel workerModel) {
        this.workerModel = workerModel;
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
