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
import androidx.recyclerview.widget.RecyclerView;

import com.example.bit_x.R;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.WorkerModel;
import com.example.bit_x.ui.AddExpenseOrDebts;
import com.example.bit_x.ui.AddTraderOrWorker;
import com.example.bit_x.ui.PayMentHistory;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder> {
    private ArrayList<WorkerModel> list = new ArrayList();
    private RecyclerViewInterface listener;

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.first_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {
        holder.workerName.setText(list.get(position).getName());
        holder.phone.setText(list.get(position).getPhoneNumber());
        holder.balance.setText(String.valueOf(list.get(position).getBalance()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList list,RecyclerViewInterface listener) {
        this.list = list;
        this.listener =listener;
        notifyDataSetChanged();
    }

    public class WorkerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnCreateContextMenuListener {
        TextView workerName;
        TextView balance;
        TextView phone;
        public WorkerViewHolder(@NonNull View v) {
            super(v);
            workerName = v.findViewById(R.id.l_Name);
            balance = v.findViewById(R.id.l_Balance);
            phone = v.findViewById(R.id.l_MobilePhone);

            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view,getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.add(getAdapterPosition(),110,0,R.string.paymentHistory);
            menu.add(getAdapterPosition(),111,1,R.string.edit);
            menu.add(getAdapterPosition(),112,2,R.string.delete);
        }
    }
    public void removeItem(int pos, FirebaseUser user){
        String key = list.get(pos).getKey();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers").child(key);
        reference.removeValue();
    }

    public void getHistory(int pos ,Context context){
        String key = list.get(pos).getKey();
        try{
            Intent intent = new Intent(context, PayMentHistory.class);
            intent.putExtra("tag", "paymentWorkerHistory");
            intent.putExtra("workerKey",key);
            context.startActivity(intent);
            ((Activity)context).finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void editItem(int pos, Context context){
        String key = list.get(pos).getKey();
      try {
          Intent intent = new Intent(context , AddTraderOrWorker.class);
          intent.putExtra("tag","editWorker");
          intent.putExtra("workerKey",key);
          context.startActivity(intent);
          ((Activity)context).finish();
      }catch (Exception e){
          e.printStackTrace();
      }
    }
}
