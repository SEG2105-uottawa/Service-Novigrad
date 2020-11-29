package com.example.novigrad.employee;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.novigrad.domain.Employee;
import com.example.novigrad.R;
import com.example.novigrad.domain.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceSelectorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceSelectorFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        db.collection(Service.COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            // Get all services
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot result = task.getResult();
                if (task.isSuccessful() && result != null) {
                    List<DocumentSnapshot> documents = result.getDocuments();
                    final ArrayList<Service> services = new ArrayList<>();
                    for (DocumentSnapshot document: documents) {
                        services.add(new Service(document));
                    }
                    db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Employee emp = new Employee(task.getResult());
                            createServiceSelectorAdapter(services, R.id.ServicesSelectRecycleView, getActivity().getApplicationContext(), emp);
                        }

                    });

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_service_selector, container, false);
    }

    private ServiceSelectorAdapter createServiceSelectorAdapter(ArrayList<Service> services, int recyclerId, Context context, Employee employee) {
        RecyclerView serviceSelectRecycler = getView().findViewById(recyclerId);
        ServiceSelectorAdapter adapter = new ServiceSelectorAdapter(context, services, employee);
        serviceSelectRecycler.setAdapter(adapter);
        serviceSelectRecycler.setLayoutManager(new LinearLayoutManager(context));
        return adapter;
    }
}