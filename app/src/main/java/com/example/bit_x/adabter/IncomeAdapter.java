package com.example.bit_x.adabter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bit_x.R;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.ClientModel;
import com.example.bit_x.models.IncomeModel;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.ui.AddTraderOrWorker;
import com.example.bit_x.ui.Income;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {
    private ArrayList<IncomeModel> list = new ArrayList();
    DatabaseReference reference;
    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IncomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.first_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        holder.incomeName.setText(list.get(position).getName());
        holder.date.setText(list.get(position).getDate());
        holder.balance.setText(String.valueOf(list.get(position).getBalance()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView incomeName;
        TextView balance;
        TextView date;
        public IncomeViewHolder(@NonNull View v) {
            super(v);
            incomeName = v.findViewById(R.id.l_Name);
            balance = v.findViewById(R.id.l_Balance);
            date = v.findViewById(R.id.l_MobilePhone);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 150, 0, R.string.edit);
            menu.add(getAdapterPosition(), 151, 1, R.string.delete);
        }
    }

    public void removeItem(int pos, FirebaseUser user ,String projectKey,Context context) {
        String key = list.get(pos).getKey();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Incomes").child(key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   double balance = snapshot.getValue(IncomeModel.class).getBalance();
                   reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
                   reference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                          if (snapshot.exists()){
                              double t = snapshot.getValue(ProjectModel.class).getIncome();
                              snapshot.getRef().child("income").setValue(t-balance);
                              ((Income)context).setCurrentIncome();
                              controlClientBalance(projectKey,balance,"-",user);
                              controlClientBalanceInProject(projectKey,balance,"-",user);
                          }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.removeValue();

    }
    public void editItem(int pos, Context context,String projectKey) {
        String key = list.get(pos).getKey();
      try {
          Intent intent = new Intent(context, AddTraderOrWorker.class);
          intent.putExtra("tag", "editIncome");
          intent.putExtra("projectKey", projectKey);
          intent.putExtra("incomeKey", key);
          context.startActivity(intent);
          ((Activity)context).finish();
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    private void controlClientBalanceInProject(String keyProject , double balance , String operation, FirebaseUser user){
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(keyProject).child("clientModel");
        reference .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if (snapshot.exists()){
                 double oldBalance = snapshot.getValue(ClientModel.class).getBalance();
                 double newBalance=0;
                 if(operation.equals("+")){
                     newBalance = balance+oldBalance;
                 }else if(operation.equals("-")){
                     newBalance = oldBalance-balance;
                 }
                 snapshot.getRef().child("balance").setValue(newBalance);
             }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    private void controlClientBalance(String keyProject,double balance ,String operation,FirebaseUser user) {
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(keyProject).child("clientModel");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   ClientModel clientModel = snapshot.getValue(ClientModel.class);
                   String key = clientModel.getKey();
                   reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Clients").child(key);
                   reference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.exists()){
                               if (snapshot.exists()){
                                   double oldBalance = snapshot.getValue(ClientModel.class).getBalance();
                                   double newBalance = 0;
                                   if (operation .equals("+")) {
                                       newBalance = balance + oldBalance;
                                   } else if (operation .equals("-")) {
                                       newBalance = oldBalance - balance;
                                   }
                                   snapshot.getRef().child("balance").setValue(newBalance);
                               }
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}