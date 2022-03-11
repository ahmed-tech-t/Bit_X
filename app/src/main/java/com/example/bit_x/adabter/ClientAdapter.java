package com.example.bit_x.adabter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bit_x.R;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.ClientModel;
import com.example.bit_x.ui.AddTraderOrWorker;
import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {
    private ArrayList<ClientModel> list = new ArrayList();
    RecyclerViewInterface listener;

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.first_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        holder.clientName.setText(list.get(position).getName());
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

    public class ClientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView clientName;
        TextView balance;
        TextView phone;

        public ClientViewHolder(@NonNull View v) {
            super(v);
            clientName = v.findViewById(R.id.l_Name);
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
            menu.add(getAdapterPosition(),120,0,R.string.edit);
            menu.add(getAdapterPosition(),121,1,R.string.delete);
        }
    }
    public void removeItem(int pos, FirebaseUser user,Context context){
        String key = list.get(pos).getKey();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Clients").child(key);
        reference.removeValue();

    }
    public void editItem(int pos , Context context){
        String key = list.get(pos).getKey();
        try {
            Intent intent = new Intent(context , AddTraderOrWorker.class);
            intent.putExtra("tag","editClient");
            intent.putExtra("clientKey",key);
            context.startActivity(intent);
            ((Activity)context).finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
