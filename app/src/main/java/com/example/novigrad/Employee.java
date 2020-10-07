package com.example.novigrad;

import com.google.firebase.firestore.DocumentSnapshot;

public class Employee extends User {
    public static String role = "Employee";

    public Employee(String firstName, String lastName, String email) {
        super(firstName, lastName, email, role);
    }

    public Employee(RegisterData registerData) {
        super(registerData);
    }

    public Employee(DocumentSnapshot userDocument) {
        super(userDocument);
    }

}
