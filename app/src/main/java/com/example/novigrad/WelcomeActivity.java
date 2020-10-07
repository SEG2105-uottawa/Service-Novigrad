package com.example.novigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class WelcomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //String user = getIntent().getStringExtra("USER");
        String role = getIntent().getStringExtra("role");
        String firstName = getIntent().getStringExtra("firstName");
        String welcome_msg = getResources().getString(R.string.welcome_msg, firstName, role);
        TextView welcomeText = (TextView) findViewById(R.id.welcomeMsg);
        welcomeText.setText(welcome_msg);
    }

    
}