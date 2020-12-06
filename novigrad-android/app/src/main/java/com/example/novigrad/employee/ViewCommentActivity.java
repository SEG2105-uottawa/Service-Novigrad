package com.example.novigrad.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.novigrad.R;
import com.example.novigrad.domain.Service;
import com.example.novigrad.domain.ServiceRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewCommentActivity extends AppCompatActivity {
    ArrayList<String> comments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comment);
        getComments();
    }

    public void getComments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(uid).collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot commentQueries = task.getResult();
                    for (DocumentSnapshot comment : commentQueries) {
                        comments.add(comment.getString("comment"));
                    }
                    createAdapter();

                }
            }
        });
    }

    public void createAdapter() {
        RecyclerView requestsRecyclerView = findViewById(R.id.CommentsRecyclerView);
        ViewCommentAdapter adapter = new ViewCommentAdapter(comments);
        requestsRecyclerView.setAdapter(adapter);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.notifyDataSetChanged();
    }
}