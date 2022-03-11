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

import com.example.bit_x.adabter.ClientAdapter;
import com.example.bit_x.databinding.ActivityClientsBinding;
import com.example.bit_x.dbHandler.DdHandler;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.ClientModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Clients extends AppCompatActivity {
    private RecyclerViewInterface listener;

    private DatabaseReference reference;
    private FirebaseAuth auth ;
    private FirebaseUser user;
    ClientAdapter adapter;
    ActivityClientsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityClientsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
        setAppLanguage();
        binding.shimmer.startShimmer();
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();
        //set Adapter
        setOnClickListener();
        getClients();


           binding.floatingButtonClient.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   try {
                   Intent intent = new Intent(Clients.this, AddTraderOrWorker.class);
                   intent.putExtra("tag","addClient");
                   startActivity(intent);
                   finish();
                   }catch (Exception e){
                       e.printStackTrace();
                   }
               }
           });
    }

    private void setOnClickListener() {
        listener =new RecyclerViewInterface() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.i(TAG, "onItemClick: "+pos);
            }

        };
    }
    private void getClients(){
        ArrayList<ClientModel> adapterList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Clients");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   adapterList.clear();
                   for (DataSnapshot data : snapshot.getChildren()) {
                       ClientModel clientModel = data.getValue(ClientModel.class);
                       clientModel.setKey(data.getKey());
                       adapterList.add(clientModel);
                   }
                   setAdapter(adapterList);
                binding.shimmer.setVisibility(View.GONE);
                   binding.shimmer.stopShimmer();
                   binding.imageView.setVisibility(View.VISIBLE);
                   binding.fabWrapper.setVisibility(View.VISIBLE);
                   if(adapterList.size()>0){
                       binding.clientRecycleView.setVisibility(View.VISIBLE);
                       binding.emptyIcon.setVisibility(View.GONE);
                   }else{
                       binding.clientRecycleView.setVisibility(View.GONE);
                       binding.emptyIcon.setVisibility(View.VISIBLE);
                   }
               }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setAdapter(ArrayList<ClientModel> clientList){
        adapter = new ClientAdapter();
        binding.clientRecycleView.setAdapter(adapter);
        adapter.setList(clientList,listener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.clientRecycleView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 120:
                try {
                    adapter.editItem(item.getGroupId(),Clients.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case 121:
                try {
                    adapter.removeItem(item.getGroupId(),user,Clients.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent intent = new Intent(Clients.this, MainActivity.class);
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