package com.example.novigrad;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Employee extends User {
    public static String role = "Employee";
    private ArrayList<DocumentReference> services;
    private ArrayList<DocumentReference> serviceRequests;
    private ArrayList<DocumentReference> customers;

    public Employee(String id, String firstName, String lastName, String email) {
        super(id, firstName, lastName, email, role);
        services = new ArrayList<>();
        serviceRequests = new ArrayList<>();
        customers = new ArrayList<>();
    }

    public Employee(String id, RegisterData registerData) {
        super(id, registerData);
        services = new ArrayList<>();
        serviceRequests = new ArrayList<>();
        customers = new ArrayList<>();
    }
    public Employee(){super();}

    public Employee(DocumentSnapshot userDocument) {
        super(userDocument);
        services = (ArrayList<DocumentReference>) userDocument.get("services");
        services = services == null ? new ArrayList<DocumentReference>() : services;
        customers = (ArrayList<DocumentReference>) userDocument.get("customers");
        customers = customers == null ? new ArrayList<DocumentReference>() : customers;
        serviceRequests = (ArrayList<DocumentReference>) userDocument.get("serviceRequests");
        serviceRequests = serviceRequests == null ? new ArrayList<DocumentReference>() : serviceRequests;

        //List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
/*        for (DocumentReference documentReference : serv) {
            Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
            tasks.add(documentSnapshotTask);
        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                for (Object object : objects) {
                    services.add(((DocumentSnapshot)object).toObject(Service.class));
                }
            }
        });*/

        /*tasks = new ArrayList<>();
        for (DocumentReference documentReference : cust) {
            Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
            tasks.add(documentSnapshotTask);
        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                for (Object object : objects) {
                    customers.add(((DocumentSnapshot)object).toObject(Customer.class));
                }
            }
        });*/


        /*tasks = new ArrayList<>();
        for (DocumentReference documentReference : reqs) {
            Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
            tasks.add(documentSnapshotTask);
        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                for (Object object : objects) {
                    serviceRequests.add(new ServiceRequest((DocumentSnapshot)object));
                }
            }
        });*/

    }

    public ArrayList<DocumentReference> getServices() {
        return services;
    }

    public ArrayList<DocumentReference> getServiceRequests() {
        return serviceRequests;
    }

    public ArrayList<DocumentReference> getCustomers() {
        return customers;
    }
}
