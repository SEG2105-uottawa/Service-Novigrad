package com.example.novigrad;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class User {
    private final String id; // Firebase uid
    private String firstName, lastName, email, role;

    public User(String uid, String firstName, String lastName, String email, String role) {
        /* Create a user from registration data */
        this.id = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public User(RegisterData registerData, String uid) {
        /* Create a user from registration data */
        this.id = uid;
        this.firstName = registerData.firstName;
        this.lastName = registerData.lastName;
        this.email = registerData.email;
        this.role = registerData.role;
    }

    public User(DocumentSnapshot userDocument) {
        /* Create a user from a firebase document (their toDocument method wasn't working) */
        this.id = userDocument.getId();
        this.firstName = (String) userDocument.get("firstName");
        this.lastName = (String) userDocument.get("lastName");
        this.email = (String) userDocument.get("email");
        this.role = (String) userDocument.get("role");
    }

    public Map<String, Object> toDocument() {
        /* Convert the user instance to a map which can be uploaded to firestore */
        Map<String, Object> document = new HashMap<>();
        document.put("firstName", this.firstName);
        document.put("lastName", this.lastName);
        document.put("email", this.email);
        document.put("role", this.role);
        return document;
    }

    public String getId() {
        return id;
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
