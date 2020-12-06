package com.example.novigrad.domain;

import android.widget.RatingBar;

import androidx.annotation.NonNull;

import com.example.novigrad.validation.ProfileData;
import com.example.novigrad.validation.RegisterData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Employee extends User {
    public static String role = "Employee";
    private ArrayList<DocumentReference> services;
    private ArrayList<DocumentReference> customers;
    private ProfileData profile;
    private double rating;

    public Employee(String id, String firstName, String lastName, String email) {
        super(id, firstName, lastName, email, role);
        services = new ArrayList<>();
        customers = new ArrayList<>();
        profile = null;
    }

    public Employee(String id, RegisterData registerData) {
        super(id, registerData);
        services = new ArrayList<>();
        customers = new ArrayList<>();
        profile = null;

    }
    public Employee(){super();}

    public Employee(DocumentSnapshot userDocument) {
        super(userDocument);
        services = (ArrayList<DocumentReference>) userDocument.get("services");
        services = services == null ? new ArrayList<DocumentReference>() : services;
        customers = (ArrayList<DocumentReference>) userDocument.get("customers");
        customers = customers == null ? new ArrayList<DocumentReference>() : customers;
        profile = ProfileData.createProfileDataFromFirebase(userDocument.get("profile"));
    }

    public ArrayList<DocumentReference> getServices() {
        return services;
    }

    public ArrayList<DocumentReference> getCustomers() {
        return customers;
    }

    public ProfileData getProfile() {
        return profile;
    }

    public void setRating(final RatingBar ratingBar) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(getId()).collection("reviews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                float ratingSum =0;

                QuerySnapshot reviews = task.getResult();
                for (DocumentSnapshot ratingDocument : reviews) {
                    ratingSum += ratingDocument.getDouble("rating").floatValue();
                }
                ratingBar.setRating(ratingSum / reviews.size());
            }
        });
    }
}
