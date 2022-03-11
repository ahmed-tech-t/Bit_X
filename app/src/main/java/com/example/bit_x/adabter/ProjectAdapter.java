package com.example.bit_x.adabter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bit_x.R;
import com.example.bit_x.interfaces.RecyclerViewInterface;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.ui.AddTraderOrWorker;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectsViewHolder> {
    private ArrayList<ProjectModel> list = new ArrayList();
    private RecyclerViewInterface listener;


    @NonNull
    @Override
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.second_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        double expense =(list.get(position).getExpense()/100*15)+list.get(position).getExpense();
        double balance = list.get(position).getIncome() - expense;
        holder.balance.setText(String.valueOf(balance));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList list, RecyclerViewInterface listener) {
        this.list = list;
        this.listener = listener;
        notifyDataSetChanged();
    }

    public class ProjectsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView name;
        TextView balance;

        public ProjectsViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.s_Name);
            balance = v.findViewById(R.id.s_Balance);
            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.add(getAdapterPosition(), 130, 0, R.string.edit);
            menu.add(getAdapterPosition(), 131, 1, R.string.delete);
        }
    }

    public void removeItem(int pos, FirebaseUser user,Context context) {
        String key = list.get(pos).getKey();
        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(key);
        //reference.removeValue();
        Toast.makeText(context,"Soon",Toast.LENGTH_LONG).show();
    }

    public void editItem(int pos, Context context) {
        String key = list.get(pos).getKey();
        try {
            Intent intent = new Intent(context, AddTraderOrWorker.class);
            intent.putExtra("tag", "editProject");
            intent.putExtra("projectKey", key);
            context.startActivity(intent);
            ((Activity)context).finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}