package com.example.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.novigrad.employee.EmployeeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class WelcomeActivity extends AppCompatActivity {
    /* View to welcome users and customers */
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Create WelcomeActivity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.getUser();
    }

    public void getUser() {
        /* Get the currently authenticated user from firebase */
        String uid = this.mAuth.getCurrentUser().getUid();
        DocumentReference userRef = this.db.collection("users").document(uid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Request is completed
                if (task.isSuccessful()) {
                    setUser(new User(task.getResult()));
                    setMessage();
                } else {
                    WelcomeActivity.this.setErrorMessage();
                }
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessage() {
        /* Set the welcome message */
        String welcomeMsg = getResources().getString(R.string.welcome_msg, user.getFirstName(), user.getRole());
        TextView welcomeTextView = (TextView) findViewById(R.id.welcomeMsg);
        welcomeTextView.setText(welcomeMsg);
    }

    public void setErrorMessage() {
        /* Set an error message */
        TextView welcomeTextView = (TextView) findViewById(R.id.welcomeMsg);
        welcomeTextView.setText(getResources().getString(R.string.error_loading_document));
    }

    public void start(View view) {
        if (user.getRole().equals("Employee")) {
            Intent intent = new Intent(this, EmployeeActivity.class);
            startActivity(intent);

        } else {

        }
    }

    public void logout(View view) {
        this.mAuth.signOut();
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    
}