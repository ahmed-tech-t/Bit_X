package com.example.bit_x.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.example.bit_x.R;
import com.example.bit_x.databinding.ActivityManageProjectBinding;
import com.example.bit_x.models.ProjectModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class ManageProject extends AppCompatActivity implements View.OnClickListener {
ActivityManageProjectBinding binding;
    ProjectModel projectModel;
    String projectKey="";
    private DatabaseReference reference;
    private FirebaseAuth auth ;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageProjectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
        setAppLanguage();
        Intent intent =getIntent();
        projectKey =intent.getStringExtra("projectKey");
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();
        binding.manageProjectExpense.setOnClickListener(this);
        binding.manageProjectIncome.setOnClickListener(this);

        getCurrentProject();

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("testtest11111");
        Intent intent =getIntent();
        projectKey =intent.getStringExtra("projectKey");
    }


    @Override
    public void onClick(View view) {
      try {
          Intent intent;
          switch (view.getId()) {
              case R.id.manageProjectIncome:
                  intent = new Intent(ManageProject.this, Income.class);
                  intent.putExtra("projectKey",projectKey);
                  startActivity(intent);
                  finish();
                  break;
              case R.id.manageProjectExpense:
                  intent = new Intent(ManageProject.this, ManageExpense.class);
                  intent.putExtra("projectKey",projectKey);
                  startActivity(intent);
                  finish();
                  break;
          }
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    private void getCurrentProject(){
       try {
           reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
           reference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.exists()){
                       projectModel = snapshot.getValue(ProjectModel.class);
                       projectModel.setKey(snapshot.getKey());
                       binding.proName.setText(projectModel.getName());
                       double expense =(projectModel.getExpense()/100*15)+projectModel.getExpense();
                       binding.proBalance.setText(String.valueOf(projectModel.getIncome()-expense));
                   }
               }
               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent intent = new Intent(this, Projects.class);
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