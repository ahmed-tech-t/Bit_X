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
import com.example.bit_x.adabter.ProjectAdapter;
import com.example.bit_x.adabter.TraderAdapter;
import com.example.bit_x.databinding.ActivityProjectsBinding;
import com.example.bit_x.interfaces.GetNameKey;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.models.TraderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Projects extends AppCompatActivity {
    ActivityProjectsBinding binding;
    ProjectAdapter adapter;
    ArrayList<ProjectModel> adapterList;

    private DatabaseReference reference;
    private FirebaseAuth auth ;
    private FirebaseUser user;

private RecyclerViewInterface listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityProjectsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
        setAppLanguage();
        binding.shimmer.startShimmer();
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();

        getProjects();
        //set Adapter
        setOnClickListener();


        binding.floatingButtonProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   Intent intent = new Intent(Projects.this, AddTraderOrWorker.class);
                   intent.putExtra("tag","addProject");
                   startActivity(intent);
                   finish();
               }catch (Exception e){
                   e.printStackTrace();
               }

            }
        });
    }


    private void setOnClickListener() {
        listener = new RecyclerViewInterface() {
            @Override
            public void onItemClick(View v, int pos) {
               try {
                   Intent intent =new Intent(Projects.this,ManageProject.class);
                   intent.putExtra("id",pos);
                   intent.putExtra("projectKey",adapterList.get(pos).getKey());
                   startActivity(intent);
                   finish();
               }catch (Exception e){
                   e.printStackTrace();
               }
            }

        };
    }
    private void getProjects(){
        adapterList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   adapterList.clear();
                   for (DataSnapshot data : snapshot.getChildren()) {
                       ProjectModel projectModel = data.getValue(ProjectModel.class);
                       projectModel.setKey(data.getKey());
                       adapterList.add(projectModel);
                   }
                   setAdapter(adapterList);
                   binding.shimmer.setVisibility(View.GONE);
                   binding.shimmer.stopShimmer();
                   binding.imageView.setVisibility(View.VISIBLE);
                   binding.projectRecycleView.setVisibility(View.VISIBLE);
                   binding.fabWrapper.setVisibility(View.VISIBLE);
                   if(adapterList.size()>0){
                       binding.projectRecycleView.setVisibility(View.VISIBLE);
                       binding.emptyIcon.setVisibility(View.GONE);
                   }else{
                       binding.projectRecycleView.setVisibility(View.GONE);
                       binding.emptyIcon.setVisibility(View.VISIBLE);
                   }
               }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case 130:
                try {
                    adapter.editItem(item.getGroupId(),Projects.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case 131:
                try {
                    adapter.removeItem(item.getGroupId(),user,Projects.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void setAdapter(ArrayList<ProjectModel> projectModel){
        adapter = new ProjectAdapter();
        binding.projectRecycleView.setAdapter(adapter);
        adapter.setList(projectModel,listener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.projectRecycleView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent intent = new Intent(Projects.this, MainActivity.class);
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