package com.example.novigrad.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.novigrad.R;
import com.example.novigrad.Service;
import com.example.novigrad.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ServiceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageServices#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageServices extends Fragment {
    Button createServiceButton;
    ServiceManagerAdapter adapter;
    FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        update();
    }

    private ServiceManagerAdapter createServiceManager(ArrayList<Service> services, Context context) {
        RecyclerView servicesRecycler = getView().findViewById(R.id.ServiceManagerRecyclerView);
        ServiceManagerAdapter adapter = new ServiceManagerAdapter(context, services);
        servicesRecycler.setAdapter(adapter);
        servicesRecycler.setLayoutManager(new LinearLayoutManager(context));
        return adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_services, container, false);
        createServiceButton = view.findViewById(R.id.addServiceButton);
        createServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServiceEditorActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update() {
        db.collection("available_services").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot result = task.getResult();
                if (task.isSuccessful() && result != null) {
                    List<DocumentSnapshot> documents = result.getDocuments();
                    ArrayList<Service> services = new ArrayList<>();
                    for (DocumentSnapshot document: documents) {
                        services.add(new Service(document));
                    }
                    if (adapter == null) {
                        adapter = createServiceManager(services, getActivity().getApplicationContext());
                    } else {
                        adapter.setServices(services);
                    }
                }
            }
        });
    }
}