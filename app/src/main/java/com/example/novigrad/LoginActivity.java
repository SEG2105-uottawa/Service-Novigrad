package com.example.novigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextInputLayout emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.emailInput = findViewById(R.id.emailInput);
        this.passwordInput = findViewById(R.id.passwordInput);
    }

    public void onLogin(final View view) {
        LoginData login = new LoginData(this);
        if (login.isAdmin()) {
            // go to admin activity
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            return;
        }

        if (!login.isValid()) {
            // Client side validation only!
            Helper.snackbar(view, "Please enter a valid email and password");
            return;
        }

        Task<AuthResult> task = this.mAuth.signInWithEmailAndPassword(login.email, login.password);
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Login successful
                    LoginActivity.this.startWelcomeActivity();
                } else {
                    // Login failed
                    Helper.snackbar(view, "Username or password is incorrect");
                }
            }
        });
    }

    public void startRegisterActivity(View view) {
        finish();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void startWelcomeActivity() {
        finish();
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }


}