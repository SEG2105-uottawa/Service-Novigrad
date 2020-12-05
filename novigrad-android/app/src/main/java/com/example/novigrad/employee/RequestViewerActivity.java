package com.example.novigrad.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.domain.ServiceRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RequestViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_viewer);

        // currently, let the customer see if their request has been approved before destroying the servicerequest instance
        // delete the request from the branch
        approveReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference serviceRequestDocument = db.collection("service_requests").document(serviceRequest.getId());
                serviceRequestDocument.update(serviceRequest.update(true, true)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Helper.snackbar(null, "Updated Request");
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
                        Helper.snackbar(null, "Updated Request");
                    }
                });
            }
        });
    }
}