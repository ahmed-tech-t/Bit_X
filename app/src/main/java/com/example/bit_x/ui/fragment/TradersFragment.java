package com.example.bit_x.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bit_x.R;
import com.example.bit_x.adabter.TraderFragmentAdapter;
import com.example.bit_x.interfaces.GetNameKey;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.ExpenseModel;
import com.example.bit_x.models.TraderModel;

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

public class TradersFragment extends Fragment {
    private DatabaseReference reference;
    private RecyclerViewInterface listener;
    private String projectKey ="";
    private FirebaseAuth auth ;
    private FirebaseUser user;
    ArrayList<TraderModel> adapterList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectKey = Debts.projectKey;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_traders, container, false);
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();
        setOnClickListener();
        getTraders(v);
        return v;
    }
    private void getTraders(View v){
        adapterList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   adapterList.clear();
                   for (DataSnapshot data: snapshot.getChildren()) {
                       ExpenseModel expenseModel = data.getValue(ExpenseModel.class);
                       if(expenseModel.getTraderModel().getBalance()<0){
                           adapterList.add(expenseModel.getTraderModel());
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


    private void setAdapter(ArrayList<TraderModel> traderList ,View v){
        TraderFragmentAdapter adapter =new TraderFragmentAdapter();
        RecyclerView recyclerView = v.findViewById(R.id.tabTradeRecycler);
        ImageView icon =v.findViewById(R.id.tabTraderIcon);
        if(adapterList.size()>0){
            icon.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            icon.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(adapter);
        adapter.setList(traderList,listener);
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
                intent.putExtra("tag","editTraderDebts");
                intent.putExtra("projectKey",projectKey);
                intent.putExtra("workerKey",adapterList.get(pos).getKey());
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);

            }
        };
    }

}