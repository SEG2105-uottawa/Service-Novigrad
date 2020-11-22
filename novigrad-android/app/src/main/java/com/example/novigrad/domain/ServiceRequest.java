package com.example.novigrad.domain;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

public class ServiceRequest {
    private static final String APPROVED_KEY = "approved";
    private DocumentReference service, customer, employee;
    private final String id;
    private boolean approved = false;


    public ServiceRequest(DocumentSnapshot requestDoc){
        this.id = requestDoc.getId();
        this.approved = (boolean) requestDoc.get(APPROVED_KEY);
        customer = (DocumentReference) requestDoc.get("customer");
        employee = (DocumentReference) requestDoc.get("employee");
        service = (DocumentReference) requestDoc.get("service");
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved){
        this.approved = approved;
    }

    public DocumentReference getService() {
        return service;
    }

    public void setService(DocumentReference service) {
        this.service = service;
    }

    public DocumentReference getCustomer() {
        return customer;
    }

    public void setCustomer(DocumentReference customer) {
        this.customer = customer;
    }

    public DocumentReference getEmployee() {
        return employee;
    }

    public void setEmployee(DocumentReference employee) {
        this.employee = employee;
    }

    @Exclude
    public String getId() {
        return id;
    }
}
