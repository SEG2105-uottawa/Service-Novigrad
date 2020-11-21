package com.example.novigrad.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.novigrad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

public class EmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);


    }

    public void onEditProfile(View view) {
        Intent intent = new Intent(this, ProfileEditorActivity.class);
        startActivity(intent);
    }

    public void onServiceManager(View view) {
        Intent intent = new Intent(this, ServiceManagerActivity.class);
        startActivity(intent);
    }

}