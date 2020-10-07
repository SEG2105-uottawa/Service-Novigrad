package com.example.novigrad;

public class User {
    private String firstName, lastName, email, role;

    User(){
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.role = "";
    }
    User(String firstName, String lastName, String email, String role) {
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
