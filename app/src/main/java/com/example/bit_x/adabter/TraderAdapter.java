package com.example.bit_x.adabter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bit_x.R;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.TraderModel;
import com.example.bit_x.ui.AddTraderOrWorker;
import com.example.bit_x.ui.Income;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TraderAdapter extends RecyclerView.Adapter<TraderAdapter.TraderViewHolder> {
    private ArrayList<TraderModel> list = new ArrayList();
    private RecyclerViewInterface listener;


    @NonNull
    @Override
    public TraderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TraderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.first_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TraderViewHolder holder, int position) {
        holder.traderName.setText(list.get(position).getName());
        holder.phone.setText(list.get(position).getPhoneNumber());
        holder.balance.setText(String.valueOf(list.get(position).getBalance()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList list,RecyclerViewInterface listener) {
        this.list = list;
        this.listener=listener;
        notifyDataSetChanged();
    }

    public class TraderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnCreateContextMenuListener {
        TextView traderName;
        TextView balance;
        TextView phone;

        public TraderViewHolder(@NonNull View v) {
            super(v);
            traderName = v.findViewById(R.id.l_Name);
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
            menu.add(getAdapterPosition(),101,0,R.string.edit);
            menu.add(getAdapterPosition(),102,1,R.string.delete);
        }
    }
    public void removeItem(int pos, FirebaseUser user){
        String key = list.get(pos).getKey();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Traders").child(key);
        reference.removeValue();
       //TODO DELETE FROM FIREBASE
    }
    public void editItem(int pos ,Context context){
        String key = list.get(pos).getKey();
      try {
          Intent intent = new Intent(context , AddTraderOrWorker.class);
          intent.putExtra("tag","editTrader");
          intent.putExtra("traderKey",key);
          context.startActivity(intent);
          ((Activity)context).finish();
      }catch (Exception e){
          e.printStackTrace();
      }
    }
}
