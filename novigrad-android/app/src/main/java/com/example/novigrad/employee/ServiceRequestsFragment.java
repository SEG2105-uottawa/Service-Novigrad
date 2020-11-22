package com.example.novigrad.employee;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.novigrad.R;
import com.example.novigrad.domain.Employee;
import com.example.novigrad.domain.ServiceRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceRequestsFragment extends Fragment {

    ServiceRequestAdapter adapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_service_requests, container, false);
    }


    private ServiceRequestAdapter createServiceReqManager(ArrayList<ServiceRequest> requests, Context context){
        RecyclerView serviceReqRecycler = getView().findViewById(R.id.ServiceRequestRecycleView);
        ServiceRequestAdapter adapter = new ServiceRequestAdapter(context, requests);
        serviceReqRecycler.setAdapter(adapter);
        serviceReqRecycler.setLayoutManager(new LinearLayoutManager(context));
        return adapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String uid = this.mAuth.getCurrentUser().getUid();
        DocumentReference userRef = this.db.collection("users").document(uid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Request is completed
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Query requests = db.collection("service_requests").whereEqualTo("employee", document.getReference()).whereEqualTo("processed", false);
                    requests.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<ServiceRequest> serviceRequests = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    serviceRequests.add(new ServiceRequest(document));
                                }
                                // Update the adapter
                                if (adapter == null) {
                                    adapter = createServiceReqManager(serviceRequests, getActivity().getApplicationContext());
                                } else {
                                    adapter.setServiceRequests(serviceRequests);
                                }
                            }
                        }
                    });
                } else {
                    System.out.println("Query Failed");
                }
            }
        });
    }
}