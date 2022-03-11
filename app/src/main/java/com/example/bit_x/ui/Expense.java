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
import com.example.bit_x.adabter.ExpenseAdapter;
import com.example.bit_x.adabter.TraderAdapter;
import com.example.bit_x.databinding.ActivityExpenseBinding;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.ExpenseModel;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.models.TraderModel;
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

public class Expense extends AppCompatActivity {
    String projectKey="";
    private RecyclerViewInterface listener;
    ActivityExpenseBinding binding;
    private DatabaseReference reference;
    private FirebaseAuth auth ;
    private FirebaseUser user;
    ExpenseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
setAppLanguage();
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();

        Intent intent =getIntent();
        projectKey = intent.getStringExtra("projectKey");
        getProjectNameAndExpenses();

        //set Adapter
       getExpenses();
        binding.floatingButtonExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   Intent intent = new Intent(Expense.this, AddExpenseOrDebts.class);
                   intent.putExtra("tag","addExpense");
                   intent.putExtra("projectKey",projectKey);
                   startActivity(intent);
                   finish();

               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        });
    }
    private void getExpenses(){
        ArrayList<ExpenseModel> adapterList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   adapterList.clear();
                   for (DataSnapshot data : snapshot.getChildren()) {
                       ExpenseModel expenseModel = data.getValue(ExpenseModel.class);
                       expenseModel.setKey(data.getKey());
                       adapterList.add(expenseModel);
                   }

                   for (int i = 0; i < adapterList.size(); i++) {
                       if (adapterList.get(i).getDebts() == 0) {
                           ExpenseModel expenseModel = adapterList.get(i);
                           for (int j = i; j > 0; j--) {
                               adapterList.set(j, adapterList.get(j - 1));
                           }
                           adapterList.set(0, expenseModel);
                       }
                   }
                   setAdapter(adapterList);
                   binding.shimmer.setVisibility(View.GONE);
                   binding.shimmer.stopShimmer();
                   binding.imageView.setVisibility(View.VISIBLE);
                   binding.expenseRecycleView.setVisibility(View.VISIBLE);
                   binding.fabWrapper.setVisibility(View.VISIBLE);
                   if(adapterList.size()>0){
                       binding.constraintLayout.setVisibility(View.VISIBLE);
                       binding.expenseRecycleView.setVisibility(View.VISIBLE);
                       binding.emptyIcon.setVisibility(View.GONE);
                   }else{
                       binding.expenseRecycleView.setVisibility(View.GONE);
                       binding.emptyIcon.setVisibility(View.VISIBLE);
                       binding.constraintLayout.setVisibility(View.GONE);

                   }
               }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setAdapter(ArrayList<ExpenseModel> expenseList){
        adapter = new ExpenseAdapter();
        binding.expenseRecycleView.setAdapter(adapter);
        adapter.setList(expenseList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.expenseRecycleView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case 160:
                try {
                    adapter.payMethod(item.getGroupId(),Expense.this,projectKey);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case 161:
                try {
                    adapter.editItem(item.getGroupId(),Expense.this,projectKey);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case 162:
                try {
                    adapter.removeItem(item.getGroupId(),user,projectKey,Expense.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
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
                  double expense = projectModel.getExpense();
                  System.out.println(expense);
                  binding.incomeProBalance.setText(String.valueOf((expense/100*15)+expense));
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
           Intent intent = new Intent(Expense.this, ManageExpense.class);
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