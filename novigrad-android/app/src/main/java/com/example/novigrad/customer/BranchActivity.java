package com.example.novigrad.customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.domain.Employee;
import com.example.novigrad.domain.Service;
import com.example.novigrad.validation.ProfileData;
import com.example.novigrad.validation.ServiceRequestData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.rpc.Help;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BranchActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference sr;

    Employee employee;
    HashMap<String, Service> services;
    Service service;
    ArrayList<String> serviceNames;

    public DatePicker dateOfBirth;
    TextView municipality, emailAndPhone, address, time, days, formTitle; // branch info
    public EditText streetNum, streetName;
    public TextInputLayout firstName, lastName;
    public RadioGroup license;
    Spinner serviceListSpinner;
    ArrayAdapter<String> adapter;
    Button submitForm;
    ImageView residenceDoc, citizenshipDoc, photoIDDoc;
    public boolean isResidenceDocAdded, isCitizenshipDocAdded, isPhotoIDDocAdded;
    private static final int RESULT_LOAD_IMAGE_RES = 1, RESULT_LOAD_IMAGE_CIT = 2, RESULT_LOAD_IMAGE_PID = 3;

    LinearLayout driversLicenseLayout, healthCardLayout, photoIDLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        sr = FirebaseStorage.getInstance().getReference();

        municipality = findViewById(R.id.branchServReqMunicipalityTextView);
        emailAndPhone = findViewById(R.id.branchServReqEmailPhoneTextView);
        address = findViewById(R.id.branchServReqAddressTextView);
        time = findViewById(R.id.branchServReqTimeTextView);
        days = findViewById(R.id.branchServReqDaysTextView);

        streetNum = findViewById(R.id.editCustStreetNum);
        streetName = findViewById(R.id.editCustStreetName);
        formTitle = findViewById(R.id.ServiceRequestTitle);
        firstName = findViewById(R.id.CustomerFirstNameInput);
        lastName = findViewById(R.id.CustomerLastNameInput);
        license = findViewById(R.id.LicenseRadioGroup);
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
        isResidenceDocAdded = false;
        citizenshipDoc = findViewById(R.id.citizenshipImageView);
        isCitizenshipDocAdded = false;
        photoIDDoc = findViewById(R.id.photoIDImageView);
        isPhotoIDDocAdded = false;

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
                    if (!isResidenceDocAdded) {
                        isResidenceDocAdded = true;
                    }
                    residenceDoc.setImageURI(selectedImage);
                    break;
                case (RESULT_LOAD_IMAGE_CIT):
                    if (!isCitizenshipDocAdded) {
                        isCitizenshipDocAdded = true;
                    }
                    citizenshipDoc.setImageURI(selectedImage);
                    break;
                case (RESULT_LOAD_IMAGE_PID):
                    if (!isPhotoIDDocAdded) {
                        isPhotoIDDocAdded = true;
                    }
                    photoIDDoc.setImageURI(selectedImage);
                    break;
            }
        }

    }

    void uploadImages(String id, String documentName, ImageView imageView) {
        if (imageView.getDrawable() == null) {
            return;
        }

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference ref = sr.child(id+"_"+documentName);

        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }

    public void onSubmit(final View view){
        ServiceRequestData data = new ServiceRequestData(this);
        if (data.isValid(view)) {
            createServiceRequest(view);
        }
    }

    private void createServiceRequest(final View view){
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
        form.put("firstName", Helper.getText(firstName));
        form.put("lastName", Helper.getText(lastName));
        form.put("streetNum", Helper.getText(streetNum));
        form.put("streetName", Helper.getText(streetName));
        form.put("license", Helper.getSelectedRadioText(license));

        DocumentReference reference = db.collection("service_requests").document();
        String id = reference.getId();
        uploadImages(id, "residence", residenceDoc);
        if (healthCardLayout.getVisibility() != View.GONE) {
            uploadImages(id, "citizenship", citizenshipDoc);
        }
        if (photoIDLayout.getVisibility() != View.GONE) {
            uploadImages(id, "photoID", photoIDDoc);
        }

        submitForm.setVisibility(View.INVISIBLE);
        db.collection("service_requests").document(id).set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Helper.snackbar(view, "Successfully added service request");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                submitForm.setVisibility(View.VISIBLE);
                Helper.snackbar(view, "Failed to add service request");
            }
        });
    }

    public boolean isRequiredDriversLicense(){
        return service.getDriversLicenseRequired();
    }
    public boolean isRequiredHealthCard(){
        return service.getHealthCardRequired();
    }
    public boolean isRequiredPhotoID(){
        return service.getPhotoIDRequired();
    }

}