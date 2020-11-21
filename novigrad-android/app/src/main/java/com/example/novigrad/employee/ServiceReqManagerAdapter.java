package com.example.novigrad.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novigrad.Customer;
import com.example.novigrad.Employee;
import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.Service;
import com.example.novigrad.ServiceRequest;
import com.example.novigrad.admin.UserManagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ServiceReqManagerAdapter extends RecyclerView.Adapter<ServiceReqManagerAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ServiceRequest> requests = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView serviceName;
        private TextView customerName;
        private Button approveReq, rejectReq;
        private Service serv;
        private Customer cust;
        private Employee employee;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.serviceName = itemView.findViewById(R.id.serviceRequestName);
            this.customerName = itemView.findViewById(R.id.customerRequestName);
            this.approveReq = itemView.findViewById(R.id.requestApproveButton);
            this.rejectReq = itemView.findViewById(R.id.requestRejectButton);
        }

        public void bind(final ServiceRequest request, final int position, final ServiceReqManagerAdapter adapter){

            request.getService().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    // Request is completed
                    if (task.isSuccessful()) {
                        serv = new Service(task.getResult());
                        serviceName.setText(serv.getName());
                    }
                }
            });

            request.getCustomer().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    // Request is completed
                    if (task.isSuccessful()) {
                        cust = new Customer(task.getResult());
                        customerName.setText(String.format("%s %s", cust.getFirstName(),  cust.getLastName()));
                    }
                }
            });

            request.getEmployee().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        employee = new Employee(task.getResult());
                    }
                }
            });

            // currently, let the customer see if their request has been approved before destroying the servicerequest instance
            // delete the request from the branch
            approveReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    request.setApproved(true);
                    DocumentReference reqDoc = db.collection("service_requests").document(request.getId());
                    employee.getCustomers().add(request.getCustomer());
                    employee.getServiceRequests().remove(reqDoc);
                    reqDoc.update("approved", request.isApproved()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Helper.snackbar(v, "Updated Request");
                        }
                    });

                    db.collection("users").document(employee.getId()).update("serviceRequests", employee.getServiceRequests()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            adapter.requests.remove(request);
                            Helper.snackbar(v, "Request approved");
                            adapter.notifyDataSetChanged();
                        }
                    });
                    // maybe make the request's reference to employee null

                }
            });

            rejectReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    request.setApproved(false);
                    DocumentReference reqDoc = db.collection("service_requests").document(request.getId());
                    employee.getServiceRequests().remove(reqDoc);
                    // maybe make the request's reference to employee null
                    reqDoc.update("approved", request.isApproved()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Helper.snackbar(v, "Updated Request");
                        }
                    });
                    db.collection("users").document(employee.getId()).update("serviceRequests", employee.getServiceRequests()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            adapter.requests.remove(request);
                            Helper.snackbar(v, "Request rejected");
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });

        }


    }
    @NonNull
    @Override
    public ServiceReqManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emp_service_request_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceReqManagerAdapter.ViewHolder holder, int position) {
        final ServiceRequest request = this.requests.get(position);
        holder.bind(request, position, this);
    }

    public ServiceReqManagerAdapter(Context context, ArrayList<ServiceRequest> requests) {
        this.context = context;
        setServiceRequests(requests);
    }

    public void setServiceRequests(ArrayList<ServiceRequest> requests) {
        this.requests = requests;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
