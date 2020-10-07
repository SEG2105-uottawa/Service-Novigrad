package com.example.novigrad;

import com.google.firebase.firestore.DocumentSnapshot;

public class Customer extends User {
    // Not used YET however in the next deliverable we will
    public static String role = "Customer";

    public Customer(String uid, String firstName, String lastName, String email) {
        super(uid, firstName, lastName, email, role);
    }

    public Customer(RegisterData registerData, String uid) {
        super(registerData, uid);
    }

    public Customer(DocumentSnapshot userDocument) {
        super(userDocument);
    }
}
