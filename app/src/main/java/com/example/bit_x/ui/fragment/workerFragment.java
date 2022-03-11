package com.example.bit_x.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.bit_x.adabter.WorkerFragmentAdapter;
import com.example.bit_x.interfaces.GetNameKey;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.WagesModel;
import com.example.bit_x.models.WorkerModel;
import com.example.bit_x.ui.AddTraderOrWorker;
import com.example.bit_x.ui.Debts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class workerFragment extends Fragment {

    private RecyclerViewInterface listener;
    private DatabaseReference reference;
    private FirebaseAuth auth ;
    ArrayList<WorkerModel> adapterList;
    private FirebaseUser user;
    private String projectKey ="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectKey = Debts.projectKey;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_worker, container, false);
        auth = FirebaseAuth.getInstance();
        user =auth.getCurrentUser();

        setOnClickListener();
        getWorkers(v);
        return v;
    }

    private void getWorkers(View v){
        adapterList = new ArrayList<>();
        System.out.println(Debts.projectKey);
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   adapterList.clear();
                   System.out.println("Workers");
                   for (DataSnapshot data: snapshot.getChildren()) {
                       WagesModel wagesModel = data.getValue(WagesModel.class);
                       if(wagesModel.getWorkerModel().getBalance()<0){
                           adapterList.add(wagesModel.getWorkerModel());
                       }
                   }
                   for (int i = 0; i < adapterList.size(); i++) {
                       for (int j = i; j < adapterList.size(); j++) {
                           if(i!=j&&adapterList.get(i).getKey().equals(adapterList.get(j).getKey())){
                               adapterList.get(i).setBalance(adapterList.get(i).getBalance()+adapterList.get(j).getBalance());
                               adapterList.remove(j);
                           }
                       }
                   }
                   setAdapter(adapterList,v);
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAdapter(ArrayList<WorkerModel> workerList ,View v){
        WorkerFragmentAdapter adapter =new WorkerFragmentAdapter();
        RecyclerView recyclerView = v.findViewById(R.id.tabWorkerRecycler);
        ImageView icon =v.findViewById(R.id.tabWorkerIcon);
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
                Intent intent =new Intent(getActivity(), AddTraderOrWorker.class);
                intent.putExtra("tag","editWorkerDebts");
                intent.putExtra("projectKey",projectKey);
                intent.putExtra("workerKey",adapterList.get(pos).getKey());
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
                ((Activity) getActivity()).finish();
            }
        };
    }
}