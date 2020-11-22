package com.example.novigrad.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.domain.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ServiceEditorActivity extends AppCompatActivity {

    TextInputLayout serviceName;
    TextInputLayout servicePrice;

    CheckBox driversLicense;
    CheckBox healthCard;
    CheckBox photoID;
    FirebaseFirestore db;
    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_editor);

        serviceName = findViewById(R.id.ServiceNameInput);
        servicePrice = findViewById(R.id.ServicePriceInput);

        driversLicense = findViewById(R.id.DriversLicenseCheckbox);
        healthCard = findViewById(R.id.HealthCardCheckbox);
        photoID = findViewById(R.id.PhotoIDCheckbox);
        db = FirebaseFirestore.getInstance();
        // if a service is being edited, get the entry from firestore with the id stored in the intent
        if (getIntent().getExtras() != null) {
            DocumentReference servRef = db.collection(Service.COLLECTION).document(getIntent().getStringExtra("id"));
            servRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    // Load the previous service's info
                    if (task.isSuccessful()) {
                        service = new Service(task.getResult());
                        serviceName.getEditText().setText(service.getName());
                        servicePrice.getEditText().setText(Double.toString(service.getPrice()));
                        driversLicense.setChecked(service.getDriversLicenseRequired());
                        healthCard.setChecked(service.getHealthCardRequired());
                        photoID.setChecked(service.getPhotoIDRequired());
                    } else {
                        System.out.println("Unsuccessful");
                    }
                }
            });

        }
    }

    public void saveService(final View view) {
        if (!canSave(view)) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("name", serviceName.getEditText().getText().toString());
        data.put("price", Double.parseDouble(servicePrice.getEditText().getText().toString()));
        data.put("driversLicense", driversLicense.isChecked());
        data.put("healthCard", healthCard.isChecked());
        data.put("photoID", photoID.isChecked());


        if (service == null) {

            db.collection("available_services").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Helper.snackbar(view, "Service successfully added.");
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Helper.snackbar(view, "Service failed to add. Try again later.");
                }
            });
        } else {
            service.setName((String)data.get("name"));
            service.setPrice((double)data.get("price"));
            service.setDriversLicenseRequired((boolean)data.get("driversLicense"));
            service.setHealthCardRequired((boolean)data.get("healthCard"));
            service.setPhotoIDRequired((boolean)data.get("photoID"));


            db.collection(Service.COLLECTION).document(service.getId()).set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Helper.snackbar(view, "Service successfully added.");
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Helper.snackbar(view, "Failed to edit service. Try again later.");
                }
            });

        }
    }

    private boolean canSave(final View view) {
        if (serviceName.getEditText().getText().toString().length() <= 0) {
            Helper.snackbar(view, "Name is required");
            return false;
        } else if (servicePrice.getEditText().getText().toString().length() <= 0) {
            Helper.snackbar(view, "Price is required");
            return false;
        } else if (!(driversLicense.isChecked() || healthCard.isChecked() || photoID.isChecked())) {
            Helper.snackbar(view, "At least one document is required");
            return false;
        }

        return true;
    }
}