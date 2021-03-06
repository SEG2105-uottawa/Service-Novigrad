package com.example.novigrad.domain;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ServiceRequest {
    private DocumentReference service, customer, employee;
    private final String id;
    private boolean processed, approved;
    private String firstName, lastName, address, license;


    public ServiceRequest(DocumentSnapshot requestDoc){
        this.id = requestDoc.getId();
        this.processed = (boolean) requestDoc.get("processed");
        this.approved = (boolean) requestDoc.get("approved");
        customer = (DocumentReference) requestDoc.get("customer");
        employee = (DocumentReference) requestDoc.get("employee");
        service = (DocumentReference) requestDoc.get("service");

        HashMap<String, String> form = (HashMap<String, String>) requestDoc.get("form");
        firstName = form.get("firstName");
        lastName = form.get("lastName");
        address = form.get("streetNum") + " " + form.get("streetName");
        license = form.get("license");
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed){
        this.processed = processed;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved){
        this.approved = approved;
    }

    public Map<String, Object> update(boolean processed, boolean approved) {
        this.processed = processed;
        this.approved = approved;
        Map<String, Object> m = new HashMap<>();
        m.put("processed", processed);
        m.put("approved", approved);
        return m;
    }

    public DocumentReference getService() {
        return service;
    }

    public DocumentReference getCustomer() {
        return customer;
    }

    public DocumentReference getEmployee() {
        return employee;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getLicense() {
        return license;
    }

    @Exclude
    public String getId() {
        return id;
    }
}
