package com.example.novigrad.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novigrad.R;
import com.example.novigrad.domain.Employee;
import com.example.novigrad.employee.ProfileActivity;
import com.example.novigrad.validation.ProfileData;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.ViewHolder> {
    private Context context;
    private Map<DocumentReference, String> serviceReferenceToName;
    private ArrayList<Employee> branches = new ArrayList<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private String id;
        private TextView municipality, emailAndPhone, address, time, days, serviceNames;
        private Button viewBranch;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            municipality = itemView.findViewById(R.id.branchMunicipalityTextView);
            emailAndPhone = itemView.findViewById(R.id.branchEmailAndPhoneTextView);
            address = itemView.findViewById(R.id.branchAddressTextView);
            time = itemView.findViewById(R.id.branchTimeTextView);
            days = itemView.findViewById(R.id.branchDaysTextView);
            viewBranch = itemView.findViewById(R.id.branchViewBtn);
            serviceNames = itemView.findViewById(R.id.branchServicesTextView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(final Employee employee, Map<DocumentReference, String> serviceReferenceToName) {
            ProfileData branch = employee.getProfile();
            municipality.setText(branch.municipality);
            emailAndPhone.setText(String.format("%s - %s", employee.getEmail(), branch.getPhone()));
            address.setText(String.format("%s %s %s", branch.streetNumber, branch.streetName, branch.postalCode));
            days.setText(android.text.TextUtils.join(", ", branch.days));

            ArrayList<String> serviceNamesList = new ArrayList<>();
            System.out.println(serviceReferenceToName.keySet());
            for (DocumentReference ref: employee.getServices()) {
                if (serviceReferenceToName.containsKey(ref)) {
                    serviceNamesList.add(serviceReferenceToName.get(ref));
                }
            }
            String serviceNamesString = android.text.TextUtils.join(", ", serviceNamesList);
            serviceNames.setText(serviceNamesString);

            viewBranch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BranchActivity.class);
                    intent.putExtra("id", employee.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_search_branch_card, parent, false);
        return new BranchAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee branch = this.branches.get(position);
        holder.bind(branch, serviceReferenceToName);
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    public BranchAdapter(Context context, ArrayList<Employee> branches, Map<DocumentReference, String> serviceReferenceToName) {
        this.context = context;
        this.serviceReferenceToName = serviceReferenceToName;
        this.setBranches(branches);
    }

    public void setBranches(ArrayList<Employee> branches) {
        this.branches = branches;
        this.notifyDataSetChanged();
    }
}
