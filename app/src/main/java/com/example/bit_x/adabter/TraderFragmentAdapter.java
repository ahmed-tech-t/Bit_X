package com.example.bit_x.adabter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bit_x.R;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.TraderModel;

import java.util.ArrayList;

public class TraderFragmentAdapter extends RecyclerView.Adapter<TraderFragmentAdapter.TraderFragmentViewHolder> {
    private ArrayList<TraderModel> list = new ArrayList();
    private RecyclerViewInterface listener;
    @NonNull
    @Override
    public TraderFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TraderFragmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.second_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TraderFragmentViewHolder holder, int position) {
        holder.traderName.setText(list.get(position).getName());
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

    public class TraderFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView traderName;
        TextView balance;
        public TraderFragmentViewHolder(@NonNull View v) {
            super(v);
            traderName = v.findViewById(R.id.s_Name);
            balance = v.findViewById(R.id.s_Balance);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try{
                listener.onItemClick(view,getAdapterPosition());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
