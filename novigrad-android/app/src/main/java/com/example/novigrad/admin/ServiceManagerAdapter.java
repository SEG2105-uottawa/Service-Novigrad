package com.example.novigrad.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novigrad.R;
import com.example.novigrad.Service;

import java.util.ArrayList;

public class ServiceManagerAdapter extends RecyclerView.Adapter<ServiceManagerAdapter.ViewHolder> {
    /* Adapter to create the admin user manager */
    private Context context;
    private ArrayList<Service> services = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView serviceName;
        private TextView servicePrice;
        private LinearLayoutCompat requiredDocumentsContainer;
        private Button userDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceNameTextView);
            servicePrice = itemView.findViewById(R.id.servicePriceTextView);
            requiredDocumentsContainer = itemView.findViewById(R.id.RequiredDocumentsContainer);
            userDelete = itemView.findViewById(R.id.userDeleteButton);
        }

        public void bind(Service service, final int position, final ServiceManagerAdapter adapter, TextView[] requiredDocuments) {
            /* Add data and delete listener to admin_manage_user.xml template  */
            final String id = service.getId();
            serviceName.setText(service.getName());
            servicePrice.setText("$"+Double.toString(service.getPrice()));

            for (TextView document : requiredDocuments) {
                if (document != null) {
                    requiredDocumentsContainer.addView(document);
                }
            }
        }
    }

    public ServiceManagerAdapter(Context context, ArrayList<Service> services) {
        this.context = context;
        this.setUsers(services);
    }

    public void setUsers(ArrayList<Service> services) {
        this.services = services;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* Recycler view method */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_manage_services, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceManagerAdapter.ViewHolder holder, int position) {
        /* Recycler view method - puts data into the layout */
        final Service service = this.services.get(position);

        TextView[] requiredDocuments = new TextView[3];
        if (service.isDriversLicenseRequired()) {
            requiredDocuments[0] = new TextView(context);
            requiredDocuments[0].setText("Driver's License");
        }
        if (service.isHealthCardRequired()) {
            requiredDocuments[1] = new TextView(context);
            requiredDocuments[1].setText("Health Card");
        }
        if (service.isPhotoIDRequired()) {
            requiredDocuments[2] = new TextView(context);
            requiredDocuments[2].setText("Photo ID");
        }

        holder.bind(service, position, this, requiredDocuments);
    }

    @Override
    public int getItemCount() {
        /* Recycler view method - get item count */
        return services.size();
    }
}
