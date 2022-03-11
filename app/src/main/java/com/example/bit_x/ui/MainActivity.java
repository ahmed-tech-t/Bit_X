package com.example.bit_x.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.example.bit_x.R;
import com.example.bit_x.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
        setAppLanguage();
        checkUser();
        binding.mainMenuProjects.setOnClickListener(this);
        binding.mainMenuWorkers.setOnClickListener(this);
        binding.mainMenuTraders.setOnClickListener(this);
        binding.mainMenuAnalise.setOnClickListener(this);
        binding.buLogout.setOnClickListener(this);

    }

    public void checkUser(){
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
           try {
               Intent intent =new Intent(MainActivity.this,Login.class);
               startActivity(intent);
               finish();
           }catch (Exception e){
               e.printStackTrace();
           }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        try {
            Intent intent;
            switch (view.getId()) {
                case R.id.mainMenuProjects:
                    intent = new Intent(MainActivity.this, Projects.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.mainMenuWorkers:
                    intent = new Intent(MainActivity.this, Workers.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.mainMenuTraders:
                    intent = new Intent(MainActivity.this, Traders.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.mainMenuAnalise:
                    intent = new Intent(MainActivity.this, Clients.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.buLogout:
                    AuthUI.getInstance().signOut(this);
                    intent =new Intent(MainActivity.this,Login.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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