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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageServices() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageServices.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageServices newInstance(String param1, String param2) {
        ManageServices fragment = new ManageServices();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateServices();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateServices();
    }

    private void updateServices() {
        FirebaseFirestore db =FirebaseFirestore.getInstance();
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
                    createServiceManager(services, getActivity().getApplicationContext());
                }
            }
        });
    }

    private ServiceManagerAdapter createServiceManager(ArrayList<Service> services, Context context) {
        RecyclerView servicesRecycler = getView().findViewById(R.id.ServiceManagerRecyclerView);
        ServiceManagerAdapter adapter = new ServiceManagerAdapter(context, services);
        servicesRecycler.setAdapter(adapter);
        servicesRecycler.setLayoutManager(new LinearLayoutManager(context));
        return adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
}