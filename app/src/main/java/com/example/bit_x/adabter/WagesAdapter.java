package com.example.bit_x.adabter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bit_x.R;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.models.WagesModel;
import com.example.bit_x.models.WorkerModel;
import com.example.bit_x.ui.AddExpenseOrDebts;
import com.example.bit_x.ui.Wages;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WagesAdapter extends RecyclerView.Adapter<WagesAdapter.WagesViewHolser> {
    private ArrayList<WagesModel> list = new ArrayList();
    DatabaseReference reference;

    @NonNull
    @Override
    public WagesViewHolser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WagesViewHolser(LayoutInflater.from(parent.getContext()).inflate(R.layout.third_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WagesViewHolser holder, int position) {
        holder.workerName.setText(list.get(position).getWorkerModel().getName());
        holder.description.setText(list.get(position).getDescription());
        holder.value.setText(String.valueOf(list.get(position).getValue()));
        holder.payed.setText(String.valueOf(list.get(position).getPayed()));
        if(list.get(position).getDebts()<=0){
            holder.debts.setText(String.valueOf(list.get(position).getDebts()*-1));
        }else if(list.get(position).getDebts()>0){
            holder.tv_balance.setText(R.string.balance);
            holder.debts.setText(String.valueOf(list.get(position).getDebts()));
        }else if(list.get(position).getDebts()==0){
            holder.debts.setText(String.valueOf(list.get(position).getDebts()));
        }
        if(list.get(position).getValue()==list.get(position).getPayed()){
            holder.constraintLayout.setBackgroundResource(R.drawable.layout_design_gray);
        }else holder.constraintLayout.setBackgroundResource(R.drawable.layout_design_white);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class WagesViewHolser extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView workerName;
        TextView description;
        TextView value;
        TextView payed;
        TextView debts;
        ConstraintLayout constraintLayout;
        TextView tv_balance;
        public WagesViewHolser(@NonNull View v) {
            super(v);
            workerName =v.findViewById(R.id.t_Name);
            description =v.findViewById(R.id.t_disc);
            value= v.findViewById(R.id.t_value);
            payed =v.findViewById(R.id.t_payed);
            debts=v.findViewById(R.id.t_debts);
            constraintLayout =v.findViewById(R.id.thirdLayout);
            tv_balance =v.findViewById(R.id.tv_balance);
            v.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 170, 0, R.string.pay);
            menu.add(getAdapterPosition(), 171, 1, R.string.edit);
            menu.add(getAdapterPosition(), 172, 2, R.string.delete);
        }

    }
    public void removeItem(int pos, FirebaseUser user , String projectKey, Context context) {
        String key = list.get(pos).getKey();
        editProjectWages(projectKey,key,user,context);
        deleteWorkerBalance(projectKey,key,user,context);
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(key);
        reference.removeValue();
    }
    public void editItem(int pos, Context context,String projectKey) {
        String key = list.get(pos).getKey();
        try{
            Intent intent = new Intent(context, AddExpenseOrDebts.class);
            intent.putExtra("tag", "editWages");
            intent.putExtra("projectKey", projectKey);
            intent.putExtra("wagesKey", key);
            context.startActivity(intent);
            ((Activity)context).finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void payMethod(int pos, Context context,String projectKey){
        String key = list.get(pos).getKey();
      try {
          Intent intent = new Intent(context, AddExpenseOrDebts.class);
          intent.putExtra("tag", "workerPayMethod");
          intent.putExtra("projectKey", projectKey);
          intent.putExtra("wagesKey", key);
          context.startActivity(intent);
          ((Activity)context).finish();
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    private void editProjectWages(String projectKey , String expenseKey , FirebaseUser user ,Context context){
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(expenseKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   double oldExpense =snapshot.getValue(WagesModel.class).getValue();
                   reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
                   reference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.exists()){
                               double projectOldExpense = snapshot.getValue(ProjectModel.class).getExpense();
                               snapshot.getRef().child("expense").setValue(projectOldExpense-oldExpense);
                               ((Wages)context).getProjectNameAndWages();
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
    private void deleteWorkerBalance(String projectKey , String wagesKey , FirebaseUser user ,Context context){
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(wagesKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    WagesModel wagesModel =snapshot.getValue(WagesModel.class);
                    double dept = wagesModel.getValue()-wagesModel.getPayed();
                    String workerKey = wagesModel.getWorkerModel().getKey();
                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers").child(workerKey);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                double workerBalance =snapshot.getValue(WorkerModel.class).getBalance();
                                snapshot.getRef().child("balance").setValue(workerBalance+dept);
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
