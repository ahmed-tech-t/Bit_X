package com.example.bit_x.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bit_x.R;
import com.example.bit_x.adabter.TraderFragmentAdapter;
import com.example.bit_x.adabter.WorkerFragmentAdapter;
import com.example.bit_x.interfaces.GetNameKey;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.models.TraderModel;
import com.example.bit_x.models.WorkerModel;
import com.example.bit_x.ui.Debts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SelectWorker extends Fragment {

    private RecyclerViewInterface listener;
    private GetNameKey namePosition;
    private DatabaseReference reference;
    private FirebaseAuth auth ;
    ArrayList<WorkerModel> adapterList;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_worker, container, false);
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();

        setOnClickListener();
        getWorkers(v);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            namePosition = (GetNameKey) activity;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void getWorkers(View v){
        adapterList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()) {
                   adapterList.clear();
                   for (DataSnapshot data : snapshot.getChildren()) {
                       WorkerModel workerModel = data.getValue(WorkerModel.class);
                       workerModel.setKey(data.getKey());
                       adapterList.add(workerModel);
                   }
               }
                   setAdapter(adapterList,v);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAdapter(ArrayList<WorkerModel> workerList ,View v){
        WorkerFragmentAdapter adapter =new WorkerFragmentAdapter();
        RecyclerView recyclerView = v.findViewById(R.id.selectWorkerRecycler);
        ImageView icon =v.findViewById(R.id.selectWorkerIcon);
        if(adapterList.size()>0){
            icon.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            icon.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(adapter);
        adapter.setList(workerList,listener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }



    private void setOnClickListener() {
        listener =new RecyclerViewInterface() {
            @Override
            public void onItemClick(View v, int pos) {
                try {
                    namePosition.getNameKey(adapterList.get(pos).getKey());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }
}