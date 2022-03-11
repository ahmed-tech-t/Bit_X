package com.example.bit_x.adabter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bit_x.R;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.ClientModel;

import java.util.ArrayList;

public class ClientFragmentAdapter extends RecyclerView.Adapter<ClientFragmentAdapter.ClientViewHolder> {
    private ArrayList<ClientModel> list = new ArrayList();
    private RecyclerViewInterface listener;

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.second_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        holder.clientName.setText(list.get(position).getName());
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

    public class ClientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView clientName;
        TextView balance;
        public ClientViewHolder(@NonNull View v) {
            super(v);
            clientName = v.findViewById(R.id.s_Name);
            balance = v.findViewById(R.id.s_Balance);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                listener.onItemClick(view,getAdapterPosition());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
