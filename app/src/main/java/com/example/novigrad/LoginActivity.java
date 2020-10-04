package com.example.novigrad;

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

    private class Login {
        String email, password;
        public Login(LoginActivity activity) {
            this.email = this.getText(activity.emailInput);
            this.password = this.getText(activity.passwordInput);
        }

        public String getText(TextInputLayout input) {
            try { return input.getEditText().getText().toString(); }
            catch (Exception e) {
                System.out.println("bruh!!!");
                return null;
            }
        }


        private boolean stringIsValid(String s) {
            return (s != null) && (s.length() > 0);
        }

        public boolean isValid() {
             return stringIsValid(email) && stringIsValid(password);
        }

        @Override
        public String toString() {
            return "Login{" +
                    "email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
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
        Login login = new Login(this);
        System.out.println(login);
        if (!login.isValid()) {
            Helper.snackbar(view, "Please enter a valid email and password");
            return;
        }

        Task<AuthResult> task = this.mAuth.signInWithEmailAndPassword(login.email, login.password);
        task.addOnCompleteListener(new LoginCompleteListener(this, view));
    }

    public void getUser(AuthResult authResult, final View view) {
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
                        System.out.println();
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