package com.example.novigrad;

import com.google.android.material.textfield.TextInputLayout;

public class LoginData {
    /* Get and validate email and password
    (validate here is just making sure the strings are not null it's not actually doing auth)*/
    String email, password;

    public LoginData(LoginActivity activity) {
        // Constructor for getting input from the login activity
        this.email = Helper.getText(activity.emailInput);
        this.password = Helper.getText(activity.passwordInput);
    }

    public LoginData(String email, String password) {
        // Constructor for test cases
        this.email = email;
        this.password = password;
    }

    public boolean isAdmin() {
        // The login is an admin
        return ("admin".equals(this.email)) && ("admin".equals(this.password));
    }
    public boolean isValid() {
        return (email != null) && (password != null) && !("".equals(email)) && !("".equals(password));
    }
}
