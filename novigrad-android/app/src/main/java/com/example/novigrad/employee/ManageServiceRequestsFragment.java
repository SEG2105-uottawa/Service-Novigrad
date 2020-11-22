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
import com.example.novigrad.domain.ServiceRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageServiceRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageServiceRequestsFragment extends Fragment {

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
                    final ArrayList<ServiceRequest> reqs = new ArrayList<>();
                    List<DocumentReference> requests = (List<DocumentReference>) document.get("serviceRequests");
                    List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                    for (DocumentReference documentReference : requests) {
                        Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                        tasks.add(documentSnapshotTask);
                    }
                    Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> objects) {
                            for (Object object : objects) {
                                reqs.add(new ServiceRequest((DocumentSnapshot)object));
                            }
                            if (adapter == null) {
                                adapter = createServiceReqManager(reqs, getActivity().getApplicationContext());
                            } else {
                                adapter.setServiceRequests(reqs);
                            }
                        }
                    });

                } else {
                    Log.d("TAG", "failed to retrieve users service requests");
                }
            }
        });
    }
}