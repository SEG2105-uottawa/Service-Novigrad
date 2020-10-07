package com.example.novigrad;

import com.google.firebase.firestore.DocumentSnapshot;

public class Employee extends User {
    // Not used YET however in the next deliverable we will
    public static String role = "Employee";

    public Employee(String uid, String firstName, String lastName, String email) {
        super(uid, firstName, lastName, email, role);
    }

    public Employee(RegisterData registerData, String uid) {
        super(registerData, uid);
    }

    public Employee(DocumentSnapshot userDocument) {
        super(userDocument);
    }

}
