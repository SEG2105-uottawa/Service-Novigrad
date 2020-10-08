package com.example.novigrad;

import android.util.Patterns;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterData {
    /* Get, store, and validate register data */
    String email, password, confirmPassword, firstName, lastName, role;

    public RegisterData(RegisterActivity activity) {
        // Constructor for getting input from the register activity
        this.firstName = this.getText(activity.firstName);
        this.lastName = this.getText(activity.lastName);
        this.email = this.getText(activity.email);
        this.password = this.getText(activity.password);
        this.confirmPassword = this.getText(activity.confirmPassword);
        this.role =  getRadioText(activity.roleSelector);
    }

    public RegisterData(String firstName, String lastName, String email, String password, String confirmPassword, String role) {
        // Constructor for test cases
        this.firstName = this.firstName;
        this.lastName = this.lastName;
        this.email = this.email;
        this.password = this.password;
        this.confirmPassword = this.confirmPassword;
        this.role = role;
    }

    public boolean isValid(View view) {
        /* Validate all register data */

        // First name validation
        if (!stringIsValid(firstName)) {
            Helper.snackbar(view, "Register Failed: First Name is missing");
            return false;
        }

        if (!nameIsValid(firstName)) {
            Helper.snackbar(view, "Register Failed: First Name contains illegal characters");
            return false;
        }

        // Last name validation
        if (!stringIsValid(lastName)) {
            Helper.snackbar(view, "Register Failed: Last Name is missing");
            return false;
        }

        if (!nameIsValid(lastName)) {
            Helper.snackbar(view, "Register Failed: Last Name contains illegal characters");
            return false;
        }

        // Email validation
        if (!stringIsValid(email)) {
            Helper.snackbar(view, "Register Failed: Email is missing");
            return false;
        }

        if (!emailIsValid()) {
            Helper.snackbar(view, "Register Failed: Email is invalid");
            return false;
        }

        // Password validation
        if (!stringIsValid(password)) {
            Helper.snackbar(view, "Register Failed: Password is missing");
            return false;
        }

        if (!passwordLengthIsValid()) {
            Helper.snackbar(view, "Register Failed: Password must be at least 6 characters");
            return false;
        }

        if (!passwordsMatchIsValid()) {
            Helper.snackbar(view, "Register Failed: Passwords do not match");
            return false;
        }

        // Role validation
        else if (!stringIsValid(this.role)) {
            Helper.snackbar(view, "Register Failed: Please select a role");
            return false;
        }

        return true;
    }

    private String getText(TextInputLayout input) {
        /* Get text from a TextInputLayout (Material Design) */
        try { return input.getEditText().getText().toString(); }
        catch (Exception e) {
            return null;
        }
    }

    private String getRadioText(RadioGroup input) {
        /* Get the selected role - Either 'Customer' or 'Employee' */
        try {
            RadioButton selected = input.findViewById(input.getCheckedRadioButtonId());
            return selected.getText().toString();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean stringIsValid(String s) {
       /* String is not null or empty */
        return (s != null) && (s.length() > 0);
    }

    private boolean emailIsValid() {
        /* Email is valid - XXXXX@XXX.XXX */
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
