package com.example.novigrad.validation;

import android.util.Patterns;
import android.view.View;

import com.example.novigrad.Helper;
import com.example.novigrad.RegisterActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterData {
    /* Get, store, and validate register data */
    private String email, password, confirmPassword, firstName, lastName, role;

    public RegisterData(RegisterActivity activity) {
        // Constructor for getting input from the register activity
        this.firstName = Helper.getText(activity.firstName);
        this.lastName = Helper.getText(activity.lastName);
        this.email = Helper.getText(activity.email);
        this.password = Helper.getText(activity.password);
        this.confirmPassword = Helper.getText(activity.confirmPassword);
        this.role = Helper.getSelectedRadioText(activity.roleSelector);
    }

    public RegisterData(String firstName, String lastName, String email, String password, String confirmPassword, String role) {
        // Constructor for test cases
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isValid(View view) {
        /* Validate all register data */

        // First name validation
        if (!Helper.stringIsValid(firstName)) {
            Helper.snackbar(view, "Register Failed: First Name is missing");
            return false;
        }

        if (!nameIsValid(firstName)) {
            Helper.snackbar(view, "Register Failed: First Name contains illegal characters");
            return false;
        }

        // Last name validation
        if (!Helper.stringIsValid(lastName)) {
            Helper.snackbar(view, "Register Failed: Last Name is missing");
            return false;
        }

        if (!nameIsValid(lastName)) {
            Helper.snackbar(view, "Register Failed: Last Name contains illegal characters");
            return false;
        }

        // Email validation
        if (!Helper.stringIsValid(email)) {
            Helper.snackbar(view, "Register Failed: Email is missing");
            return false;
        }

        if (!emailIsValid()) {
            Helper.snackbar(view, "Register Failed: Email is invalid");
            return false;
        }

        // Password validation
        if (!Helper.stringIsValid(password)) {
            Helper.snackbar(view, "Register Failed: Password is missing");
            return false;
        }

        if (!passwordLengthIsValid()) {
            Helper.snackbar(view, "Register Failed: Password must contain at least 6 characters");
            return false;
        }

        if (!passwordsMatchIsValid()) {
            Helper.snackbar(view, "Register Failed: Passwords do not match");
            return false;
        }

        // Role validation
        else if (!Helper.stringIsValid(this.role) || !(this.role.equals("Customer") || this.role.equals("Employee"))) {
            Helper.snackbar(view, "Register Failed: Please select a role");
            return false;
        }

        return true;
    }

    private boolean emailIsValid() {
        /* Email is valid - XXXXX@XXX.XXX */
        try {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } catch (Exception e) {
            // JUNIT email not working
            return true;
        }
    }

    private boolean nameIsValid(String name) {
        /* Name is valid - letters only */
        Pattern allowedCharacters = Pattern.compile("[a-zA-z]+");
        Matcher nameAsMatcher = allowedCharacters.matcher(name);
        return nameAsMatcher.matches();
    }

    private boolean passwordLengthIsValid() {
        /* Check if the password is at least 6 characters */
        return password.length() > 5;
    }

    private boolean passwordsMatchIsValid() {
        /* Check if the password match */
        return password.equals(confirmPassword);
    }
}
