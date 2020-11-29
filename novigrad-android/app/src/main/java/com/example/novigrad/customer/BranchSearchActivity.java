package com.example.novigrad.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.novigrad.R;
import com.example.novigrad.domain.Customer;
import com.example.novigrad.domain.Employee;
import com.example.novigrad.employee.ServiceSelectorAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BranchSearchActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ArrayList<Employee> employees;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_search);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        employees = new ArrayList<>();
        getBranches();
    }

    public void getBranches() {
        db.collection("users").whereEqualTo("role", Employee.role).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                QuerySnapshot results = task.getResult();
                for (DocumentSnapshot document: results.getDocuments()) {
                    Employee employee = new Employee(document);
                    if (employee.getProfile() != null) {
                        employees.add(employee);
                    }
                }
                createAdapter();
            }
        });
    }
    public BranchAdapter createAdapter() {
        RecyclerView branchRecyclerView = findViewById(R.id.BranchSearchRecyclerView);
        BranchAdapter adapter = new BranchAdapter(getApplicationContext(), employees);
        branchRecyclerView.setAdapter(adapter);
        branchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        return adapter;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}