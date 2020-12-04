package com.example.novigrad.customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.domain.Employee;
import com.example.novigrad.domain.Service;
import com.example.novigrad.domain.ServiceRequest;
import com.example.novigrad.validation.ProfileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BranchActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    Employee employee;
    HashMap<String, Service> services;
    Service service;
    ArrayList<String> serviceNames;

    DatePicker dateOfBirth;
    TextView municipality, emailAndPhone, address, time, days, streetName, formTitle; // branch info
    TextInputLayout firstName, lastName;
    Spinner serviceListSpinner;
    ArrayAdapter<String> adapter;
    Button submitForm;
    ImageView residenceDoc, citizenshipDoc, photoIDDoc;
    private static final int RESULT_LOAD_IMAGE_RES = 1, RESULT_LOAD_IMAGE_CIT = 2, RESULT_LOAD_IMAGE_PID = 3;

    LinearLayout driversLicenseLayout, healthCardLayout, photoIDLayout, formLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        municipality = findViewById(R.id.branchServReqMunicipalityTextView);
        emailAndPhone = findViewById(R.id.branchServReqEmailPhoneTextView);
        address = findViewById(R.id.branchServReqAddressTextView);
        time = findViewById(R.id.branchServReqTimeTextView);
        days = findViewById(R.id.branchServReqDaysTextView);
        streetName = findViewById(R.id.editCustStreetName);
        formTitle = findViewById(R.id.ServiceRequestTitle);
        firstName = findViewById(R.id.CustomerFirstNameInput);
        lastName = findViewById(R.id.CustomerLastNameInput);
        dateOfBirth = findViewById(R.id.CustomerDOBDatePicker);
        serviceListSpinner = (Spinner) findViewById(R.id.serviceSelectSpinner);

        driversLicenseLayout = findViewById(R.id.LicenseFormLayout);
        healthCardLayout = findViewById(R.id.HealthCardFormLayout);
        photoIDLayout = findViewById(R.id.PhotoIDLayout);
        submitForm = findViewById(R.id.custSubmitServReqBtn);


        String uid = (String)getIntent().getExtras().get("id");
        services = new HashMap<>();
        serviceNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, serviceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceListSpinner.setAdapter(adapter);

        residenceDoc = findViewById(R.id.residenceImageView);
        citizenshipDoc = findViewById(R.id.citizenshipImageView);
        photoIDDoc = findViewById(R.id.photoIDImageView);

        residenceDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_RES);
            }
        });

        citizenshipDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_CIT);
            }
        });

        photoIDDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_PID);
            }
        });


        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) { // need to add user documents and info
                Map<String, Object> request = new HashMap<>();
                Map<String, Object> form = new HashMap<>();
                request.put("approved", false);
                request.put("processed", false);
                request.put("customer", db.collection("users").document(mAuth.getCurrentUser().getUid()));
                request.put("employee", db.collection("users").document(employee.getId()));
                request.put("service", db.collection(Service.COLLECTION).document(service.getId()));
                request.put("form", form);
                String dob = dateOfBirth.getDayOfMonth() + "/" + dateOfBirth.getMonth() + "/" +
                        dateOfBirth.getYear();
                form.put("dob", dob);

                db.collection("service_requests").add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Helper.snackbar(v, "Successfully added service request");
                    }
                });
                System.out.println("SHOULD WORK!");
            }
        });

        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    employee = new Employee(task.getResult());
                    ProfileData branch = employee.getProfile();
                    municipality.setText(branch.getMunicipality());
                    emailAndPhone.setText(String.format("%s - %s", employee.getEmail(), branch.getPhone()));
                    address.setText(String.format("%s %s %s", branch.streetNumber, branch.streetName, branch.postalCode));
                    days.setText(android.text.TextUtils.join(", ", branch.days));

                    ArrayList<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                    for (DocumentReference doc : employee.getServices()){
                        Task<DocumentSnapshot> documentSnapshotTask = doc.get();
                        tasks.add(documentSnapshotTask);
                    }
                    Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> objects) {
                            for (Object object : objects) {
                                DocumentSnapshot document = (DocumentSnapshot) object;
                                if (document.getData() != null) {
                                    Service serv = new Service(document);
                                    services.put(serv.getName(), serv);
                                    serviceNames.add(serv.getName());
                                }
                            }
                            adapter.notifyDataSetChanged();
                            serviceListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String serviceName = parent.getItemAtPosition(position).toString();
                                    Helper.snackbar(view, String.format("Retrieved %s request form", serviceName));

                                    formTitle.setText(String.format("%s Request Form", serviceName));
                                    service = services.get(serviceName);
                                    if (service.getDriversLicenseRequired()) {
                                        driversLicenseLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        driversLicenseLayout.setVisibility(View.GONE);
                                    }
                                    if (service.getHealthCardRequired()) {
                                        healthCardLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        healthCardLayout.setVisibility(View.GONE);
                                    }
                                    if (service.getPhotoIDRequired()) {
                                        photoIDLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        photoIDLayout.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // do nothing
                                }
                            });
                        }
                    });


                }

            }
        });
        serviceListSpinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            switch(requestCode) {
                case (RESULT_LOAD_IMAGE_RES):
                    residenceDoc.setImageURI(selectedImage);
                    break;
                case (RESULT_LOAD_IMAGE_CIT):
                    citizenshipDoc.setImageURI(selectedImage);
                    break;
                case (RESULT_LOAD_IMAGE_PID):
                    photoIDDoc.setImageURI(selectedImage);
                    break;
            }
        }

    }
}