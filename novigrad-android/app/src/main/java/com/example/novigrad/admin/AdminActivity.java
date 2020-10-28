package com.example.novigrad.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.novigrad.R;
import com.example.novigrad.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            // Get all users
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot result = task.getResult();
                if (task.isSuccessful() && result != null) {
                    List<DocumentSnapshot> documents = result.getDocuments();
                    ArrayList<User> users = new ArrayList<>();
                    for (DocumentSnapshot document: documents) {
                        users.add(new User(document));
                    }
                    createUserManager(users, R.id.userManagerRecyclerView, getApplicationContext());
                }
            }
        });
    }

    private UserManagerAdapter createUserManager(ArrayList<User> users, int recyclerId, Context context) {
        /* Create a user manager adapter*/
        RecyclerView usersRecycler = findViewById(recyclerId);
        UserManagerAdapter adapter = new UserManagerAdapter(context, users);
        usersRecycler.setAdapter(adapter);
        usersRecycler.setLayoutManager(new LinearLayoutManager(context));
        return adapter;
    }
}