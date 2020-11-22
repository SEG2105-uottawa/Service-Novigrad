package com.example.novigrad.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novigrad.domain.Employee;
import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.domain.Service;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class ServiceSelectorAdapter extends RecyclerView.Adapter<ServiceSelectorAdapter.ViewHolder>{
    private Context context;
    private static Employee employee;
    private static ArrayList<DocumentReference> empServices;
    private ArrayList<Service> services = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView serviceName;
        private TextView servicePrice;
        private CheckBox serviceSelected;
        private FirebaseFirestore db;
        private FirebaseAuth mAuth;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            serviceName = itemView.findViewById(R.id.serviceNameSelect);
            serviceSelected = itemView.findViewById(R.id.selectServiceBox);
            servicePrice = itemView.findViewById(R.id.servicePrice);

            empServices = employee.getServices();
            /*String uid = this.mAuth.getCurrentUser().getUid();
            DocumentReference userRef = this.db.collection("users").document(uid);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    // Request is completed
                    if (task.isSuccessful()) {
                        employee = new Employee(task.getResult());
                        services = employee.getServices();

                    } else {
                        Helper.snackbar(itemView, "Cannot retrieve data");
                    }
                }
            });*/
        }
        // known issue where employee is not updated from both adapters; only updated certain fields to fix this problem
        public void bind(final Service service, final int position, final ServiceSelectorAdapter adapter) {
            serviceName.setText(service.getName());
            servicePrice.setText(String.format("$ %.2f", service.getPrice()));

            final DocumentReference servRef = this.db.collection(Service.COLLECTION).document(service.getId());

            if (employee != null) {
                for (DocumentReference doc: empServices) {
                    if (service.getId().equals(doc.getId())) {
                        serviceSelected.setChecked(true);
                    }
                }
            }
            serviceSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        empServices.add(servRef);
                    } else {
                        empServices.remove(servRef);
                    }
                    adapter.notifyDataSetChanged();
                    db.collection("users").document(employee.getId()).update("services", empServices).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Helper.snackbar(buttonView, "Updated services");
                        }
                    });

                }

            });
        }
    }
    public ServiceSelectorAdapter(Context context, ArrayList<Service> services, Employee employee) {
        this.context = context;
        this.setServices(services);
        this.employee = employee;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ServiceSelectorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_service_select_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceSelectorAdapter.ViewHolder holder, int position) {
        final Service service = this.services.get(position);
        holder.bind(service, position, this);

    }

    @Override
    public int getItemCount() {
        return services.size();
    }


}
