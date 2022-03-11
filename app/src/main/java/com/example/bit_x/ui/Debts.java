package com.example.bit_x.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.example.bit_x.R;
import com.example.bit_x.adabter.TabLayoutAdapter;
import com.example.bit_x.databinding.ActivityDebtsBinding;
import com.example.bit_x.interfaces.GetNameKey;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.ui.fragment.TradersFragment;
import com.example.bit_x.ui.fragment.workerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Debts extends AppCompatActivity {
    public static String projectKey="";
    private DatabaseReference reference;
    private FirebaseAuth auth ;
    private FirebaseUser user;
    ActivityDebtsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDebtsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
        setAppLanguage();
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();
        Intent intent =getIntent();
        projectKey = intent.getStringExtra("projectKey");
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        TabLayoutAdapter adapter =new TabLayoutAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new workerFragment(), "الصنايعية");
        adapter.addFragment(new TradersFragment(),"التجار");
        binding.viewPager.setAdapter(adapter);
        getProjectNameAndExpenses();
    }
    public void getProjectNameAndExpenses(){
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProjectModel projectModel= snapshot.getValue(ProjectModel.class);
                binding.proName.setText(projectModel.getName());
                double expense = projectModel.getExpense();
                System.out.println(expense);
                binding.proBalance.setText(String.valueOf((expense/100*15)+expense));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
      Intent intent = new Intent(Debts.this, ManageExpense.class);
        intent.putExtra("projectKey",projectKey);
        startActivity(intent);
        finish();
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