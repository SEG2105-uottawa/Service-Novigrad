package com.example.novigrad;

import com.google.firebase.firestore.DocumentSnapshot;

public class Employee extends User {
    // Not used YET however in the next deliverable we will
    public static String role = "Employee";

    public Employee(String id, String firstName, String lastName, String email) {
        super(id, firstName, lastName, email, role);
    }

    public Employee(String id, RegisterData registerData) {
        super(id, registerData);
    }

    public Employee(DocumentSnapshot userDocument) {
        super(userDocument);
    }

}
