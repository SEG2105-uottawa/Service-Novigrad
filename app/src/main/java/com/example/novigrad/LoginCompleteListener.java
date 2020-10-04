package com.example.novigrad;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginCompleteListener implements OnCompleteListener<AuthResult> {
    private LoginActivity activity;
    private View view;

    public LoginCompleteListener(LoginActivity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Login successful
            this.activity.getUser(task.getResult(), this.view);
        } else {
            // Login failed
            Helper.snackbar(this.view, "Username or password is incorrect");
        }
    }
}
