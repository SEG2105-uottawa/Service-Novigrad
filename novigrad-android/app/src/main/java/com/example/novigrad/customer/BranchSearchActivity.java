package com.example.novigrad.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.novigrad.R;
import com.example.novigrad.domain.Customer;
import com.example.novigrad.domain.Employee;
import com.example.novigrad.domain.Service;
import com.example.novigrad.employee.ServiceSelectorAdapter;
import com.example.novigrad.validation.ProfileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BranchSearchActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ArrayList<Employee> employees;
    Map<String, DocumentReference> serviceNameToReference;
    Map<DocumentReference, String> serviceReferenceToName;
    Button searchButton;
    BranchAdapter adapter;

    public ArrayList<Employee> search() {
        TextInputEditText searchEditText = findViewById(R.id.searchEditText);
        String text = searchEditText.getText().toString().toLowerCase();

        CheckBox sunday, monday, tuesday, wednesday, thursday, friday, saturday;
        sunday = findViewById(R.id.sunday);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);

        ArrayList<Employee> results = new ArrayList<>();
        for (Employee e: employees) {
            ProfileData p = e.getProfile();
            if (p.getMunicipality().toLowerCase().equals(text) || p.getPostalCode().toLowerCase().equals(text) || p.getStreetNumber().equals(text)) {
                results.add(0, e); // first result
            } else if (e.getServices().contains(serviceNameToReference.get(text))) {
                // contains the requested service
                results.add(0, e);
            } else {
                // Check for a matching day
                if (sunday.isChecked() && !p.days.contains("Sunday")) {
                    continue;
                } else if (monday.isChecked() && !p.days.contains("Monday")) {
                    continue;
                }  else if (tuesday.isChecked() && !p.days.contains("Tuesday")) {
                    continue;
                }  else if (wednesday.isChecked() && !p.days.contains("Wednesday")) {
                    continue;
                }  else if (thursday.isChecked() && !p.days.contains("Thursday")) {
                    continue;
                }  else if (friday.isChecked() && !p.days.contains("Friday")) {
                    continue;
                }  else if (saturday.isChecked() && !p.days.contains("Saturday")) {
                    continue;
                }
                results.add(e);
            }
        }
        return results;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_search);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employees != null) {
                    adapter.setBranches(search());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        getData();
    }

    public void getData() {
        Task<QuerySnapshot> getEmployees = db.collection("users").whereEqualTo("role", Employee.role).get();
        Task<QuerySnapshot> getServices = db.collection("available_services").get();

        ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();
        tasks.add(getEmployees);
        tasks.add(getServices);
        Tasks.whenAllComplete(tasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                // Get employees task
                Task<QuerySnapshot> getEmployees = (Task<QuerySnapshot>) task.getResult().get(0);
                employees = new ArrayList<>();
                QuerySnapshot results = getEmployees.getResult();
                for (DocumentSnapshot document: results.getDocuments()) {
                    Employee employee = new Employee(document);
                    if (employee.getProfile() != null) {
                        employees.add(employee);
                    }
                }

                // Get services task
                Task<QuerySnapshot> getServices = (Task<QuerySnapshot>) task.getResult().get(1);
                serviceNameToReference = new HashMap<>();
                serviceReferenceToName = new HashMap<>();

                results = getServices.getResult();
                for (DocumentSnapshot document: results.getDocuments()) {
                    Service service = new Service(document);
                    serviceNameToReference.put(service.getName().toLowerCase(), document.getReference());
                    serviceReferenceToName.put(document.getReference(), service.getName());
                }

                adapter = createAdapter();
            }
        });

    }

    public BranchAdapter createAdapter() {
        RecyclerView branchRecyclerView = findViewById(R.id.BranchSearchRecyclerView);
        BranchAdapter adapter = new BranchAdapter(getApplicationContext(), employees, serviceReferenceToName);
        branchRecyclerView.setAdapter(adapter);
        branchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        return adapter;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}