package com.example.bit_x.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.example.bit_x.R;
import com.example.bit_x.databinding.ActivityManageExpenseBinding;
import com.example.bit_x.models.ProjectModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class ManageExpense extends AppCompatActivity implements View.OnClickListener {
ActivityManageExpenseBinding binding;
String projectKey="";
    private DatabaseReference reference;
    private FirebaseAuth auth ;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageExpenseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setAppLanguage();
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();
        Intent intent =getIntent();
        projectKey = intent.getStringExtra("projectKey");
        this.setContentView(view);
        binding.expense.setOnClickListener(this);
       // binding.debts.setOnClickListener(this);
        binding.wages.setOnClickListener(this);
        getProjectNameAndExpenses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent =getIntent();
        projectKey = intent.getStringExtra("projectKey");
    }

    @Override
    public void onClick(View view) {
      try {
          Intent intent;
          switch (view.getId()) {
              case R.id.expense:
                  intent = new Intent(ManageExpense.this, Expense.class);
                  intent.putExtra("projectKey",projectKey);
                  startActivity(intent);
                  finish();
                  break;
           /* case R.id.debts:
                intent = new Intent(ManageExpense.this, Debts.class);
                intent.putExtra("projectKey",projectKey);
                startActivity(intent);
                finish();
                break;*/
              case R.id.wages:
                  intent = new Intent(ManageExpense.this, Wages.class);
                  intent.putExtra("projectKey",projectKey);
                  startActivity(intent);
                  finish();
                  break;
          }
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    public void getProjectNameAndExpenses(){
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   ProjectModel projectModel= snapshot.getValue(ProjectModel.class);
                   binding.proName.setText(projectModel.getName());
                   double expense =(projectModel.getExpense()/100*15)+projectModel.getExpense();

                   binding.proBalance.setText(String.valueOf(projectModel.getIncome()-expense));
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      try {
          Intent intent = new Intent(ManageExpense.this, ManageProject.class);
          intent.putExtra("projectKey",projectKey);
          startActivity(intent);
          finish();
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    private void setAppLanguage(){
        String languageToLoad = "english"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

}