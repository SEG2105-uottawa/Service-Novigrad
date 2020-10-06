package com.example.novigrad;

import android.util.Patterns;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class RegisterData {
    /* Get and validate email and password
    (validate here is just making sure the strings are not null it's not actually doing auth)*/

    String email, password, confirmPassword;
    String firstName, lastName;
    public RegisterData(RegisterActivity activity) {
        this.firstName = this.getText(activity.firstName);
        this.lastName = this.getText(activity.lastName);
        this.email = this.getText(activity.email);
        this.password = this.getText(activity.password);
        this.confirmPassword = this.getText(activity.confirmPassword);
    }

    public boolean isValid(View view) {
        if (!emailIsValid()) {
            Helper.snackbar(view, "Email invalid");
        }
        return stringIsValid(email) && stringIsValid(password);
    }

    @Override
    public String toString() {
        return "Login{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    private String getText(TextInputLayout input) {
        try { return input.getEditText().getText().toString(); }
        catch (Exception e) {
            System.out.println("bruh!!!");
            return null;
        }
    }

    private boolean stringIsValid(String s) {
        return (s != null) && (s.length() > 5);
    }

    public boolean emailIsValid() {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }

        return true;
    }

}
