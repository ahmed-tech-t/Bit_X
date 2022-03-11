package com.example.bit_x.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class ProjectModel {
    @Exclude
    String key;
    String name;//*
    double income;
    double expense;
    ClientModel clientModel;//*
    ArrayList<TraderModel> traderList;
    ArrayList<WorkerModel> workerList;

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }


    public ProjectModel(String name, ClientModel clientModel) {
        this.name = name;
        this.clientModel = clientModel;
    }

    public ProjectModel(String name, double income) {
        this.name = name;
        this.income = income;
    }

    public ProjectModel() {
    }

    public ProjectModel(String name, double income, double expense, ClientModel clientModel) {
        this.name = name;
        this.income = income;
        this.expense = expense;
        this.clientModel = clientModel;
    }




    public void addTrader(TraderModel traderModel){
        traderList.add(traderModel);
    }
    public void addWorker(WorkerModel workerModel){
        workerList.add(workerModel);
    }
    public ArrayList<TraderModel> getTraderList() {
        return traderList;
    }

    public void setTraderList(ArrayList<TraderModel> traderList) {
        this.traderList = traderList;
    }

    public ArrayList<WorkerModel> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(ArrayList<WorkerModel> workerList) {
        this.workerList = workerList;
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



    public ClientModel getClientModel() {
        return clientModel;
    }

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }


}
