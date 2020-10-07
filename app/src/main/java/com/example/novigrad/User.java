package com.example.novigrad;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String firstName, lastName, email, role;

    public User(RegisterData registerData) {
        this.firstName = registerData.firstName;
        this.lastName = registerData.lastName;
        this.email = registerData.email;
        this.role = registerData.role;
    }

    public User(DocumentSnapshot userDocument) {
        this.firstName = (String) userDocument.get("firstName");
        this.lastName = (String) userDocument.get("lastName");
        this.email = (String) userDocument.get("email");
        this.role = (String) userDocument.get("role");
    }

    public Map<String, Object> toDocument() {
        Map<String, Object> document = new HashMap<>();
        document.put("firstName", this.firstName);
        document.put("lastName", this.lastName);
        document.put("email", this.email);
        document.put("role", this.role);
        return document;
    }

    public User(String firstName, String lastName, String email, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getRole() {
        return role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format("%s: %s %s (%s)", getRole(), getFirstName(), getLastName(), getEmail());
    }
}
