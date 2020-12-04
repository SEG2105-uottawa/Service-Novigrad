package com.example.novigrad.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.novigrad.R;
import com.example.novigrad.domain.Service;
import com.example.novigrad.domain.ServiceRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomerServiceRequestsActivity extends AppCompatActivity {
    ArrayList<ServiceRequest> serviceRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_requests);
        getServiceRequests();
    }

    public void getServiceRequests() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        final DocumentReference customerRef = db.collection("users").document(uid);
        Query query = db.collection("service_requests").whereEqualTo("customer", customerRef);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    serviceRequests = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        serviceRequests.add(new ServiceRequest(document));
                    }
                    createAdapter();
                }
            }
        });
    }

    public void createAdapter() {
        RecyclerView requestsRecyclerView = findViewById(R.id.RequestsRecyclerView);
        CustomerServiceRequestAdapter adapter = new CustomerServiceRequestAdapter(serviceRequests);
        requestsRecyclerView.setAdapter(adapter);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.notifyDataSetChanged();
    }
}