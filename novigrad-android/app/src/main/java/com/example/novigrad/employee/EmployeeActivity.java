package com.example.novigrad.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.novigrad.R;

public class EmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
    }

    public void onEditProfile(View view) {
        Intent intent = new Intent(this, ProfileEditorAcitivty.class);
        startActivity(intent);
    }
}