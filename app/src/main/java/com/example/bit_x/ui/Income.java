package com.example.bit_x.ui;

import static android.content.ContentValues.TAG;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.bit_x.R;
import com.example.bit_x.adabter.IncomeAdapter;
import com.example.bit_x.databinding.ActivityIncomeBinding;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.IncomeModel;
import com.example.bit_x.models.ProjectModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Income extends AppCompatActivity {
    ActivityIncomeBinding binding;
    private DatabaseReference reference;
    private FirebaseAuth auth ;
    private FirebaseUser user;
    IncomeAdapter adapter;
    String projectKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityIncomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
        setAppLanguage();
        Intent intent =getIntent();
        projectKey = intent.getStringExtra("projectKey");


        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();
        binding.shimmer.startShimmer();
        //set Adapter
        getIncome(projectKey);

        binding.floatingButtonIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              try {
                  Intent intent = new Intent(Income.this, AddTraderOrWorker.class);
                  intent.putExtra("tag","addIncome");
                  intent.putExtra("projectKey",projectKey);
                  startActivity(intent);
                  finish();
              }catch (Exception e){
                  e.printStackTrace();
              }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ProjectModel projectModel =snapshot.getValue(ProjectModel.class);
                    binding.proName.setText(projectModel.getName());
                    binding.incomeProBalance.setText(String.valueOf(projectModel.getIncome()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getIncome(String key){
        ArrayList<IncomeModel> adapterList = new ArrayList<>();
        if(key !=null){
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(key).child("Incomes");
            reference.keepSynced(true);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()) {
                       adapterList.clear();
                       for (DataSnapshot data : snapshot.getChildren()) {
                           IncomeModel incomeModel = data.getValue(IncomeModel.class);
                           incomeModel.setKey(data.getKey());
                           adapterList.add(incomeModel);
                       }
                       setCurrentIncome();
                   }
                       setAdapter(adapterList);
                       binding.shimmer.setVisibility(View.GONE);
                       binding.shimmer.stopShimmer();
                       binding.imageView.setVisibility(View.VISIBLE);
                       binding.incomeRecycleView.setVisibility(View.VISIBLE);
                       binding.fabWrapper.setVisibility(View.VISIBLE);
                       if(adapterList.size()>0){
                           binding.incomeRecycleView.setVisibility(View.VISIBLE);
                           binding.emptyIcon.setVisibility(View.GONE);
                           binding.constraintLayout.setVisibility(View.VISIBLE);
                       }else{
                           binding.incomeRecycleView.setVisibility(View.GONE);
                           binding.emptyIcon.setVisibility(View.VISIBLE);
                           binding.constraintLayout.setVisibility(View.GONE);

                       }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void setAdapter(ArrayList<IncomeModel> incomeModel){
        adapter = new IncomeAdapter();
        binding.incomeRecycleView.setAdapter(adapter);
        adapter.setList(incomeModel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.incomeRecycleView.setLayoutManager(linearLayoutManager);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case 150:
                try {
                    adapter.editItem(item.getGroupId(),Income.this,projectKey);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case 151:
                try {
                    adapter.removeItem(item.getGroupId(),user,projectKey,Income.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
                setCurrentIncome();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent intent = new Intent(Income.this, ManageProject.class);
            intent.putExtra("projectKey",projectKey);
            startActivity(intent);
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void setCurrentIncome (){
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProjectModel projectModel =snapshot.getValue(ProjectModel.class);
                binding.proName.setText(projectModel.getName());
                binding.incomeProBalance.setText(String.valueOf(projectModel.getIncome()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
