package com.example.novigrad.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileEditorAcitivty extends AppCompatActivity {

    EditText streetNumber, streetName, postalCode, municipality, phone;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor_acitivty);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        streetNumber = findViewById(R.id.streetNumEditText);
        streetName = findViewById(R.id.streetNameEditText);
        postalCode = findViewById(R.id.postalEditText);
        municipality = findViewById(R.id.municipalityEditText);
        phone = findViewById(R.id.phoneEditText);
    }

    public void onSaveProfile(final View view) {
        ProfileData profileData = new ProfileData(this);
        if (profileData.isValid(view)) {
            db.collection("users").document(mAuth.getUid()).update("profile", profileData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Helper.snackbar(view, getResources().getString(R.string.profile_save_success));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Helper.snackbar(view, getResources().getString(R.string.profile_save_failed));
                }
            });
        }
    }
}