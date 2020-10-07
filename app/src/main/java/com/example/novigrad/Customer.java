package com.example.novigrad;

import com.google.firebase.firestore.DocumentSnapshot;

public class Customer extends User{
    public static String role = "Customer";

    public Customer(String firstName, String lastName, String email) {
        super(firstName, lastName, email, role);
    }

    public Customer(RegisterData registerData) {
        super(registerData);
    }

    public Customer(DocumentSnapshot userDocument) {
        super(userDocument);
    }
}
