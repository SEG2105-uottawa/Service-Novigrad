package com.example.novigrad.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.WelcomeActivity;
import com.example.novigrad.domain.Employee;
import com.example.novigrad.domain.User;
import com.example.novigrad.validation.ProfileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getEmployee();
    }

    public void onEditProfile(View view) {
        Intent intent = new Intent(this, ProfileEditorActivity.class);
        startActivity(intent);
    }

    public void getEmployee() {
        /* Get the currently authenticated user from firebase */
        String uid = this.mAuth.getCurrentUser().getUid();
        DocumentReference userRef = this.db.collection("users").document(uid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Request is completed
                if (task.isSuccessful()) {
                    Employee employee = new Employee(task.getResult());
                    employee.setRating(findViewById(R.id.RatingBar));
                    if (employee.getProfile() == null) {
                        createErrorMessage();
                    } else {
                        createProfileCard(employee);
                    }
                } else {
                    createErrorMessage();
                }
            }
        });
    }

    public void createProfileCard(Employee employee) {
        ProfileData profile = employee.getProfile();
        TextView phone, address, hours;
        LinearLayout error, info;
        phone = findViewById(R.id.profilePhone);
        address = findViewById(R.id.profileAddress);
        hours = findViewById(R.id.profileWorkingHours);
        error = findViewById(R.id.profileErrorLayout);
        info = findViewById(R.id.profileInfoLayout);
        phone.setText(String.format("Call: %s", profile.getPhone()));
        address.setText(String.format("%s %s, %s (%s)", profile.streetNumber, profile.streetName, profile.municipality, profile.getPostalCode()));
        String days = android.text.TextUtils.join(", ", profile.days);
        hours.setText(String.format("Working Hours: %s - %s on %s", Helper.convertTimeToString(profile.fromTime), Helper.convertTimeToString(profile.toTime), days));
        error.setVisibility(View.GONE);
        info.setVisibility(View.VISIBLE);
    }

    public void createErrorMessage() {
        LinearLayout error, info;
        error = findViewById(R.id.profileErrorLayout);
        info = findViewById(R.id.profileInfoLayout);
        error.setVisibility(View.VISIBLE);
        info.setVisibility(View.GONE);
    }

    public void onServiceManager(View view) {
        Intent intent = new Intent(this, ServiceManagerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        getEmployee();
    }

    public void onViewCommments(View view) {
        Intent intent = new Intent(this, ViewCommentActivity.class);
        startActivity(intent);
    }

}