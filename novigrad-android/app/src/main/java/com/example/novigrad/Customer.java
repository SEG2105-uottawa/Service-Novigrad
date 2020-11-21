package com.example.novigrad;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class Customer extends User {
    // Not used YET however in the next deliverable we will
    public static String role = "Customer";
    private ArrayList<ServiceRequest> serviceRequests;

    public Customer(String uid, String firstName, String lastName, String email) {
        super(uid, firstName, lastName, email, role);
        serviceRequests = new ArrayList<>();
    }

    public Customer(String id, RegisterData registerData) {
        super(id, registerData);
        serviceRequests = new ArrayList<>();
    }

    public Customer(DocumentSnapshot userDocument) {
        super(userDocument);
        serviceRequests = new ArrayList<>();
    }
    public Customer(){
        super();
    }

    public ArrayList<ServiceRequest> getServiceRequests() {
        return serviceRequests;
    }
}
