package com.example.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    /* Registers a user */
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
        // Get firebase instances
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();

        // Get inputs
        this.firstName = findViewById(R.id.firstNameInput);
        this.lastName = findViewById(R.id.lastNameInput);
        this.email = findViewById(R.id.emailInput);
        this.password = findViewById(R.id.passwordInput);
        this.confirmPassword = findViewById(R.id.confirmPasswordInput);
        this.roleSelector = findViewById(R.id.accountRadioGroup);
    }

    public void onRegister(View view) {
        /* Handle Register Click
        * 1. create the user in firebase auth (also signs them in)
        * 2. create user document in firebase firestore
        * 3. send the user to the welcome activity
        * */
        final RegisterData registerData = new RegisterData(this);
        if (registerData.isValid(view)) {
            this.createUserAuth(registerData, view);
        }
    }

    public void createUserAuth(final RegisterData registerData, final View view) {
        /* Create the user in Firebase Auth */
        mAuth.createUserWithEmailAndPassword(registerData.email, registerData.password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
        Map<String, Object> userDocument = new User(registerData).toDocument();

        // Upload to firebase
        String uid = authResult.getUser().getUid();
        DocumentReference userRef = db.collection("users").document(uid);
        userRef.set(userDocument).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    RegisterActivity.this.startWelcomeActivity();
                } else {
                    Helper.snackbar(view, "Register failed: "+ task.getException().getMessage());
                }
            }
        });
    }

    public void startWelcomeActivity() {
        /* Start the welcome activity */
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}