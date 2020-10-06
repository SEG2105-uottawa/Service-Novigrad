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

        this.db = FirebaseFirestore.getInstance();

        this.mAuth = FirebaseAuth.getInstance();
        this.firstName = findViewById(R.id.firstNameInput);
        this.lastName = findViewById(R.id.lastNameInput);
        this.email = findViewById(R.id.emailInput);
        this.password = findViewById(R.id.passwordInput);
        this.confirmPassword = findViewById(R.id.confirmPasswordInput);
        this.roleSelector = findViewById(R.id.accountRadioGroup);
    }

    public void onRegister(final View view) {
        RegisterData registerData = new RegisterData(this);
        boolean isValid = registerData.isValid(view);
        if (isValid) {
            mAuth.createUserWithEmailAndPassword(this.email.getEditText().getText().toString(), this.password.getEditText().getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Helper.snackbar(view,"Register successful");
                        addUser(task.getResult(), view);
                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        startActivity(intent);
                    } else {
                        Helper.snackbar(view, "Register failed: "+ task.getException().getMessage());
                    }
                }
            });
        }
    }

    public void addUser(AuthResult authResult, final View view) {
        RegisterData registerData = new RegisterData(this);
        Map<String, String> userObj = new HashMap<>();
        userObj.put("email", registerData.email);
        userObj.put("firstName", registerData.firstName);
        userObj.put("lastName", registerData.lastName);
        userObj.put("role", registerData.roleSelected);
        String userId = authResult.getUser().getUid();
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.set(userObj).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Success!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failure!");
            }
        });
    }
}