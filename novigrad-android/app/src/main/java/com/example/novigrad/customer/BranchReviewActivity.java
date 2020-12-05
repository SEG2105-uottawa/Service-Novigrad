package com.example.novigrad.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.novigrad.R;
import com.example.novigrad.domain.Employee;
import com.example.novigrad.validation.ProfileData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class BranchReviewActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private TextView municipality, emailAndPhone, address, time, days;
    private RatingBar review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_review);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        municipality = findViewById(R.id.branchMunicipalityTextView);
        emailAndPhone = findViewById(R.id.branchEmailAndPhoneTextView);
        address = findViewById(R.id.branchAddressTextView);
        time = findViewById(R.id.branchTimeTextView);
        days = findViewById(R.id.branchDaysTextView);
        review = findViewById(R.id.ratingBar);

        review.setStepSize(1);

        final String employeeId = getIntent().getStringExtra("id");

        db.collection("users").document(employeeId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Employee employee = new Employee(documentSnapshot);

                ProfileData branch = employee.getProfile();
                municipality.setText(branch.municipality);
                emailAndPhone.setText(String.format("%s - %s", employee.getEmail(), branch.getPhone()));
                address.setText(String.format("%s %s %s", branch.streetNumber, branch.streetName, branch.postalCode));
                days.setText(android.text.TextUtils.join(", ", branch.days));
            }
        });

        db.collection("users").document(employeeId).collection("reviews").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("rating") != null)
                review.setRating(documentSnapshot.getDouble("rating").floatValue());
            }
        });

        review.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                HashMap<String, Float> data = new HashMap<>();
                data.put("rating", rating);
                db.collection("users").document(employeeId).collection("reviews").document(mAuth.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("SUCCESS!");
                    }
                });
            }
        });
    }
}