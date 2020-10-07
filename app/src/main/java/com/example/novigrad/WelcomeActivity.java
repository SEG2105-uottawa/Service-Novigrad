package com.example.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class WelcomeActivity extends AppCompatActivity {
    /* View to welcome users and customers */
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Create WelcomeActivity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.getUser();
    }

    public void getUser() {
        /* Get the currently authenticated user from firebase */
        String uid = this.auth.getCurrentUser().getUid();
        DocumentReference userRef = this.db.collection("users").document(uid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Request is completed
                if (task.isSuccessful()) {
                    User user = new User(task.getResult());
                    WelcomeActivity.this.setMessage(user);
                } else {
                    WelcomeActivity.this.setErrorMessage();
                }
            }
        });
    }

    public void setMessage(User user) {
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

    
}