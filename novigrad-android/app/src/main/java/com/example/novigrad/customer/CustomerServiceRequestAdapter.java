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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.domain.Customer;
import com.example.novigrad.domain.Employee;
import com.example.novigrad.domain.Service;
import com.example.novigrad.domain.ServiceRequest;
import com.example.novigrad.employee.ServiceRequestAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CustomerServiceRequestAdapter extends RecyclerView.Adapter<CustomerServiceRequestAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ServiceRequest> serviceRequests;

    public CustomerServiceRequestAdapter(ArrayList<ServiceRequest> serviceRequests) {
        this.serviceRequests = serviceRequests;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView serviceName, employeeName, status;
        private Service service;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.serviceName = itemView.findViewById(R.id.serviceRequestName);
            this.employeeName = itemView.findViewById(R.id.employeeRequestName);
            this.status = itemView.findViewById(R.id.status);
        }

        public void bind(final ServiceRequest serviceRequest) {
            serviceRequest.getService().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    // Request is completed
                    if (task.isSuccessful()) {
                        service = new Service(task.getResult());
                        serviceName.setText(service.getName());
                        if (!serviceRequest.isProcessed()) {
                            status.setText("Processing");
                            status.setTextColor(ContextCompat.getColor(ViewHolder.this.itemView.getContext(), R.color.colorProcessing));
                        } else if (serviceRequest.isApproved()) {
                            status.setText("Approved");
                            status.setTextColor(ContextCompat.getColor(ViewHolder.this.itemView.getContext(), R.color.colorAccepted));
                        } else {
                            status.setText("Rejected");
                            status.setTextColor(ContextCompat.getColor(ViewHolder.this.itemView.getContext(), R.color.colorRejected));
                        }
                    }
                }
            });

            serviceRequest.getEmployee().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    // Request is completed
                    if (task.isSuccessful()) {
                        Employee e = new Employee(task.getResult());
                        employeeName.setText(String.format("Branch: %s", e.getEmail()));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_service_request_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ServiceRequest request = this.serviceRequests.get(position);
        holder.bind(request);
    }

    public void setServiceRequests(ArrayList<ServiceRequest> serviceRequests) {
        this.serviceRequests = serviceRequests;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return serviceRequests.size();
    }
}
