package com.example.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.Help;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    TextInputLayout firstName;
    TextInputLayout lastName;
    TextInputLayout email;
    TextInputLayout password;
    TextInputLayout confirmPassword;
    RadioGroup roleSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.mAuth = FirebaseAuth.getInstance();
        this.firstName = findViewById(R.id.firstNameInput);
        this.lastName = findViewById(R.id.lastNameInput);
        this.email = findViewById(R.id.emailInput);
        this.password = findViewById(R.id.passwordInput);
        this.confirmPassword = findViewById(R.id.confirmPasswordInput);
        this.roleSelector = findViewById(R.id.accountRadioGroup);
    }

    public void onRegister(final View view) {
        LoginData login = new LoginData(this);
        System.out.println(email.getEditText().getText().toString() + " " + password.getEditText().getText().toString());
        mAuth.createUserWithEmailAndPassword(this.email.getEditText().getText().toString(), this.password.getEditText().getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Helper.snackbar(view,"Register successful");
                    FirebaseUser user = mAuth.getCurrentUser();
                } else {
                    Helper.snackbar(view, "Register failed");
                    System.out.println(task.getException());
                }
            }
        });
    }
}