package com.example.bit_x.dbHandler;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bit_x.models.ClientModel;
import com.example.bit_x.models.ExpenseModel;
import com.example.bit_x.models.IncomeModel;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.models.TraderModel;
import com.example.bit_x.models.WagesModel;
import com.example.bit_x.models.WorkerModel;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class DdHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
