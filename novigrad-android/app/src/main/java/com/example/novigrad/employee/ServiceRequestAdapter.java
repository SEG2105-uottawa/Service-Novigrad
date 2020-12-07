package com.example.novigrad.employee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novigrad.domain.Customer;
import com.example.novigrad.R;
import com.example.novigrad.domain.Service;
import com.example.novigrad.domain.ServiceRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ServiceRequestAdapter extends RecyclerView.Adapter<ServiceRequestAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ServiceRequest> serviceRequests;

    public ServiceRequestAdapter(ArrayList<ServiceRequest> serviceRequests) {
        this.serviceRequests = serviceRequests;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView serviceName;
        private TextView customerName;
        private Button viewReqButton;
        private Service service;
        private Customer cust;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.serviceName = itemView.findViewById(R.id.serviceRequestName);
            this.customerName = itemView.findViewById(R.id.customerRequestName);
            this.viewReqButton = itemView.findViewById(R.id.requestViewButton);
        }

        public void bind(final ServiceRequest serviceRequest, final int position, final ServiceRequestAdapter adapter){
            serviceRequest.getService().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    // Request is completed
                    if (task.isSuccessful()) {
                        service = new Service(task.getResult());
                        serviceName.setText(service.getName());
                    }
                }
            });

            customerName.setText(String.format("%s %s", serviceRequest.getFirstName(),  serviceRequest.getLastName()));

            viewReqButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RequestViewerActivity.class);
                    intent.putExtra("id", serviceRequest.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public ServiceRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_service_request_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceRequestAdapter.ViewHolder holder, int position) {
        final ServiceRequest request = this.serviceRequests.get(position);
        holder.bind(request, position, this);
    }

    public ServiceRequestAdapter(Context context, ArrayList<ServiceRequest> requests) {
        this.context = context;
        setServiceRequests(requests);
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
