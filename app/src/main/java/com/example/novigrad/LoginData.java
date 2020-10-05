package com.example.novigrad;

import com.google.android.material.textfield.TextInputLayout;

public class LoginData {
    /* Get and validate email and password
    (validate here is just making sure the strings are not null it's not actually doing auth)*/

    String email, password;
    public LoginData(LoginActivity activity) {
        this.email = this.getText(activity.emailInput);
        this.password = this.getText(activity.passwordInput);
    }

    public boolean isAdmin() {
        // The login is an admin
        return ("admin".equals(this.email)) && ("admin".equals(this.password));
    }
    public boolean isValid() {
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
}
