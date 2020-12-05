package com.example.novigrad.shared;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.domain.Service;
import com.example.novigrad.domain.ServiceRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RequestViewerActivity extends AppCompatActivity {
    StorageReference sr = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Button approveReq, rejectReq;
    TextView firstName, lastName, address, license;
    ImageView residencePhoto, citizenshipPhoto, photoIDPhoto;
    LinearLayout licenseLayout, citizenshipLayout, photoIDLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_viewer);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        approveReq = findViewById(R.id.approveReq);
        rejectReq = findViewById(R.id.rejectReq);

        //mandatory fields
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        address = findViewById(R.id.address);
        residencePhoto = findViewById(R.id.residencePhoto);

        //not mandatory fields (rendered conditionally)
        license = findViewById(R.id.license);
        citizenshipPhoto = findViewById(R.id.citizenshipPhoto);
        photoIDPhoto = findViewById(R.id.photoIDPhoto);

        //layouts for rendering conditionally
        licenseLayout = findViewById(R.id.licenseLayout);
        citizenshipLayout = findViewById(R.id.citizenshipLayout);
        photoIDLayout = findViewById(R.id.photoIDLayout);

        licenseLayout.setVisibility(View.INVISIBLE);
        citizenshipLayout.setVisibility(View.INVISIBLE);
        photoIDLayout.setVisibility(View.INVISIBLE);

        final String id = getIntent().getStringExtra("id");
        db.collection("service_requests").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final ServiceRequest serviceRequest = new ServiceRequest(task.getResult());
                    firstName.setText(serviceRequest.getFirstName());
                    lastName.setText(serviceRequest.getLastName());
                    address.setText(serviceRequest.getAddress());
                    renderImage(id, residencePhoto, "residence");

                    serviceRequest.getService().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> serviceTask) {
                            Service service = new Service(serviceTask.getResult());

                            if (service.getDriversLicenseRequired()) {
                                license.setText(serviceRequest.getLicense());
                                licenseLayout.setVisibility(View.VISIBLE);
                            }
                            if (service.getHealthCardRequired()) {
                                renderImage(id, citizenshipPhoto, "citizenship");
                                licenseLayout.setVisibility(View.VISIBLE);
                            }
                            if (service.getPhotoIDRequired()) {
                                renderImage(id, photoIDPhoto, "photoID");
                                photoIDLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    // currently, let the customer see if their request has been approved before destroying the servicerequest instance
                    // delete the request from the branch
                    approveReq.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference serviceRequestDocument = db.collection("service_requests").document(serviceRequest.getId());
                            System.out.println("test");
                            serviceRequestDocument.update(serviceRequest.update(true, true)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Helper.snackbar(v, "Updated Request");
                                }
                            });
                        }
                    });

                    rejectReq.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference serviceRequestDocument = db.collection("service_requests").document(serviceRequest.getId());
                            serviceRequestDocument.update(serviceRequest.update(true, false)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Helper.snackbar(v, "Updated Request");
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void renderImage(String id, final ImageView view, String document) {
        StorageReference imageRef = sr.child(id+"_"+document);
        final long TEN_MB = 10 * 1024 * 1024;

        imageRef.getBytes(TEN_MB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                view.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("SHOULD WORK!");
            }
        });
    }
}