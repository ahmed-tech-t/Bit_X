package com.example.bit_x.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.bit_x.R;
import com.example.bit_x.databinding.ActivityPayMentHistoryBinding;

import java.util.Locale;

public class PayMentHistory extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityPayMentHistoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityPayMentHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
        setAppLanguage();
        setSpinner();


    }

    private void setAppLanguage(){
        String languageToLoad = "english"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0){
            System.out.println(position);
        }else if(position ==1){
            System.out.println(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void setSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.paymentSpinner.setAdapter(adapter);
        binding.paymentSpinner.setOnItemSelectedListener(this);
    }
}