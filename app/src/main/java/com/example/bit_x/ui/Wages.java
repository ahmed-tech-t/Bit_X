package com.example.bit_x.ui;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.example.bit_x.adabter.WagesAdapter;

import com.example.bit_x.databinding.ActivityWagesBinding;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.models.WagesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Wages extends AppCompatActivity {
    ActivityWagesBinding binding;
    String projectKey="";
    private RecyclerViewInterface listener;
    private DatabaseReference reference;
    private FirebaseAuth auth ;
    private FirebaseUser user;
    WagesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWagesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
        setAppLanguage();
        auth = FirebaseAuth.getInstance();
        user =auth.getCurrentUser();

        Intent intent =getIntent();
        projectKey = intent.getStringExtra("projectKey");
        getProjectNameAndWages();

        //set Adapter
        getWages();
        binding.floatingButtonWages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   Intent intent = new Intent(Wages.this, AddExpenseOrDebts.class);
                   intent.putExtra("tag","addWages");
                   intent.putExtra("projectKey",projectKey);
                   startActivity(intent);
                   finish();
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        });
    }
    private void getWages(){
        ArrayList<WagesModel> adapterList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  adapterList.clear();
                  for (DataSnapshot data : snapshot.getChildren()) {
                      WagesModel wagesModel = data.getValue(WagesModel.class);
                      wagesModel.setKey(data.getKey());
                      adapterList.add(wagesModel);
                  }

                  for (int i = 0; i < adapterList.size(); i++) {
                      if (adapterList.get(i).getDebts() == 0) {
                          WagesModel wagesModel = adapterList.get(i);
                          for (int j = i; j > 0; j--) {
                              adapterList.set(j, adapterList.get(j - 1));
                          }
                          adapterList.set(0, wagesModel);
                      }
                  }
                  setAdapter(adapterList);
                  binding.shimmer.setVisibility(View.GONE);
                  binding.shimmer.stopShimmer();
                  binding.imageView.setVisibility(View.VISIBLE);
                  binding.wagesRecycleView.setVisibility(View.VISIBLE);
                  binding.fabWrapper.setVisibility(View.VISIBLE);
                  if(adapterList.size()>0){
                      binding.wagesRecycleView.setVisibility(View.VISIBLE);
                      binding.emptyIcon.setVisibility(View.GONE);
                      binding.constraintLayout.setVisibility(View.VISIBLE);

                  }else{
                      binding.wagesRecycleView.setVisibility(View.GONE);
                      binding.emptyIcon.setVisibility(View.VISIBLE);
                      binding.constraintLayout.setVisibility(View.GONE);

                  }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setAdapter(ArrayList<WagesModel> wagesList){
        adapter = new WagesAdapter();
        binding.wagesRecycleView.setAdapter(adapter);
        adapter.setList(wagesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.wagesRecycleView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case 170:
            try {
                adapter.payMethod(item.getGroupId(),Wages.this,projectKey);
            }catch (Exception e){
                e.printStackTrace();
            }
                return true;
            case 171:
            try {
                adapter.editItem(item.getGroupId(),Wages.this,projectKey);
            }catch (Exception e){
                e.printStackTrace();
            }
                return true;
            case 172:
            try {
                adapter.removeItem(item.getGroupId(),user,projectKey,Wages.this);
            }catch (Exception e){
                e.printStackTrace();
            }
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
    public void getProjectNameAndWages(){
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   ProjectModel projectModel= snapshot.getValue(ProjectModel.class);
                   binding.proName.setText(projectModel.getName());
                   double expense = projectModel.getExpense();
                   binding.ProBalance.setText(String.valueOf((expense/100*15)+expense));
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
           Intent intent = new Intent(Wages.this, ManageExpense.class);
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