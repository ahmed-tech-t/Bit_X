package com.example.bit_x.adabter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bit_x.R;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.WorkerModel;

import java.util.ArrayList;

public class WorkerFragmentAdapter extends RecyclerView.Adapter<WorkerFragmentAdapter.WorkerFragmentViewHolder> {
    private ArrayList<WorkerModel> list = new ArrayList();
    private RecyclerViewInterface listener;
    @NonNull
    @Override
    public WorkerFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkerFragmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.second_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerFragmentViewHolder holder, int position) {
        holder.workerName.setText(list.get(position).getName());
        holder.balance.setText(String.valueOf(list.get(position).getBalance()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList list , RecyclerViewInterface listener) {
        this.listener =listener;
        this.list = list;
        notifyDataSetChanged();
    }

    public class WorkerFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView workerName;
        TextView balance;
        public WorkerFragmentViewHolder(@NonNull View v) {
            super(v);
            workerName = v.findViewById(R.id.s_Name);
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
