package com.example.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    /* Registers a user */
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextInputLayout firstName, lastName, email, password, confirmPassword;
    RadioGroup roleSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Get firebase instances
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();

        // Get inputs
        this.firstName = findViewById(R.id.firstNameInput);
        this.lastName = findViewById(R.id.lastNameInput);
        this.email = findViewById(R.id.ServiceNameInput);
        this.password = findViewById(R.id.ServicePriceInput);
        this.confirmPassword = findViewById(R.id.confirmPasswordInput);
        this.roleSelector = findViewById(R.id.accountRadioGroup);
    }

    public void onRegister(View view) {
        /* Handle Register Click
        * 1. create the user in firebase auth (also signs them in)
        * 2. create user document in firebase firestore
        * 3. send the user to the welcome activity
        * */
        RegisterData registerData = new RegisterData(this);
        if (registerData.isValid(view)) {
            this.createUserAuth(registerData, view);
        }
    }

    public void createUserAuth(final RegisterData registerData, final View view) {
        /* Create the user in Firebase Auth */
        mAuth.createUserWithEmailAndPassword(registerData.getEmail(), registerData.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    AuthResult authResult = task.getResult();
                    RegisterActivity.this.createUserDocument(registerData, authResult, view);
                } else {
                    Helper.snackbar(view, "Register failed: "+ task.getException().getMessage());
                }
            }
        });
    }

    public void createUserDocument(RegisterData registerData, AuthResult authResult, final View view) {
        /* Create the user in Firebase Fire store */
        String uid = authResult.getUser().getUid();
        final User user = new User(uid, registerData);

        // Upload to firebase
        final DocumentReference userRef = db.collection("users").document(user.getId());
        userRef.set(user.toDocument()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (user.getRole().equals("Employee")) {
                        userRef.update("services", new ArrayList<>());
                        userRef.update("serviceRequests", new ArrayList<>());
                        userRef.update("customers", new ArrayList<>());
                    } else {
                        userRef.update("serviceRequests",new ArrayList<>());
                    }
                    RegisterActivity.this.startWelcomeActivity();
                } else {
                    Helper.snackbar(view, "Register failed: "+ task.getException().getMessage());
                    // If the document can't be created delete the user from firebase auth
                    RegisterActivity.this.mAuth.getCurrentUser().delete();
                }
            }
        });
    }

    public void startWelcomeActivity() {
        /* Start the welcome activity */
        finish();
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void startLoginActivity(View view) {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}