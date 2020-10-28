package com.example.novigrad.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.novigrad.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ServiceEditorActivity extends AppCompatActivity {

    TextInputLayout serviceName;
    TextInputLayout servicePrice;

    CheckBox driversLicense;
    CheckBox healthCard;
    CheckBox photoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_editor);

        serviceName = findViewById(R.id.ServiceNameInput);
        servicePrice = findViewById(R.id.ServicePriceInput);

        driversLicense = findViewById(R.id.DriversLicenseCheckbox);
        healthCard = findViewById(R.id.HealthCardCheckbox);
        photoID = findViewById(R.id.PhotoIDCheckbox);
    }

    public void saveService(View view) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", serviceName.getEditText().getText().toString());
        data.put("price", Integer.parseInt(servicePrice.getEditText().getText().toString()));
        data.put("driversLicense", driversLicense.isChecked());
        data.put("healthCard", healthCard.isChecked());
        data.put("photoID", photoID.isChecked());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("available_services").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("Successfully added service");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error adding service " + e);
            }
        });
    }
}