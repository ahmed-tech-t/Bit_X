package com.example.bit_x.ui;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bit_x.R;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginRegisterActivity";
    int AUTHUI_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setAppLanguage();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            setLogin();
        }
    }
    public void setLogin(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        //build layout
        AuthMethodPickerLayout authUiLayout = new AuthMethodPickerLayout
                .Builder(R.layout.google_layout)
                .setGoogleButtonId(R.id.buLogin)
                .build();

        //build google login
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.Theme_Bit_X)
                .setAuthMethodPickerLayout(authUiLayout)
                .setAlwaysShowSignInMethodScreen(true)
                .setIsSmartLockEnabled(false)
                .build();
        startActivityForResult(intent, AUTHUI_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // We have signed in the user or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: " + user.toString());
                //Checking for User (New/Old)

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();

            } else {
                // Signing in failed
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                        finish();
                } else {
                    Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    AuthUI.getInstance().signOut(this);
                    setLogin();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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