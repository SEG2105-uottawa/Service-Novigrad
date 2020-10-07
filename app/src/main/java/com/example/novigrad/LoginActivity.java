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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
        System.out.println(login);
        if (login.isAdmin()) {
            // go to admin activity
            Helper.snackbar(view, "Admin has logged in");
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
        task.addOnCompleteListener(new LoginCompleteListener(this, view));

    }

    public void startRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void startWelcomeActivity(AuthResult authResult, final View view, final Intent intent) {
        String userId = authResult.getUser().getUid();
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("User exists in firebase");
                        String login = String.format("%s: %s %s (%s)", document.get("role"), document.get("firstName"), document.get("lastName"), document.get("email"));
                        Helper.snackbar(view, login);
                        intent.putExtra("role", (String) document.get("role"));
                        intent.putExtra("firstName", (String) document.get("firstName"));
                        intent.putExtra("lastName", (String) document.get("lastName"));
                        intent.putExtra("email", (String) document.get("email"));
                        startActivity(intent);
                    } else {
                        System.out.println("User does not exists in firebase");
                    }
                } else {
                    System.out.println("Failed with " + task.getException().getMessage());
                }
            }
        });

    }


}