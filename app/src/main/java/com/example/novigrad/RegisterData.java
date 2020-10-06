package com.example.novigrad;

import android.util.Patterns;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;
import com.google.rpc.Help;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterData {
    /* Get and validate email and password
    (validate here is just making sure the strings are not null it's not actually doing auth)*/

    String email, password, confirmPassword;
    String firstName, lastName;
    String roleSelected;
    public RegisterData(RegisterActivity activity) {
        this.firstName = this.getText(activity.firstName);
        this.lastName = this.getText(activity.lastName);
        this.email = this.getText(activity.email);
        this.password = this.getText(activity.password);
        this.confirmPassword = this.getText(activity.confirmPassword);
        this.roleSelected =  getRadioText(activity.roleSelector);
    }

    public boolean isValid(View view) {
        if (!nameIsValid(firstName)) {
            Helper.snackbar(view, "First Name invalid");
            return false;
        } else if (!nameIsValid(lastName)) {
            Helper.snackbar(view, "Last Name invalid");
            return false;
        } else if (!emailIsValid()) {
            Helper.snackbar(view, "Email invalid");
            return false;
        } else if (!passwordLengthIsValid()) {
            Helper.snackbar(view, "Password must be longer than 5 characters");
            return false;
        } else if (!passwordIsValid()) {
            Helper.snackbar(view, "Passwords do not match");
            return false;
        } else if (!stringIsValid(this.roleSelected)) {
            Helper.snackbar(view, "Select a role");
            return false;
        }

        return true;
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

    private String getRadioText(RadioGroup input) {
        try {
            RadioButton selected = input.findViewById(input.getCheckedRadioButtonId());
            return selected.getText().toString();
        } catch (Exception e) {
            System.out.println("RadioText bad");
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

    public boolean nameIsValid(String name) {
        Pattern allowedCharacters = Pattern.compile("[a-zA-z]+");
        Matcher nameAsMatcher = allowedCharacters.matcher(name);
        return nameAsMatcher.matches();
    }

    public boolean passwordLengthIsValid() {
        return password.length() > 5;
    }

    public boolean passwordIsValid() {
        return password.equals(confirmPassword);
    }
}
