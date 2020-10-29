package com.example.novigrad.admin;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.Service;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ServiceManagerAdapter extends RecyclerView.Adapter<ServiceManagerAdapter.ViewHolder> {
    /* Adapter to create the admin service manager */
    private Context context;
    private ArrayList<Service> services = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView serviceName;
        private TextView servicePrice;
        private TextView serviceRequiredDocuments;
        private Button serviceDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceNameTextView);
            servicePrice = itemView.findViewById(R.id.servicePriceTextView);
            serviceRequiredDocuments = itemView.findViewById(R.id.serviceRequiredDocumentsTextView);
            serviceDelete = itemView.findViewById(R.id.serviceDeleteButton);
        }

        public void bind(Service service, final int position, final ServiceManagerAdapter adapter) {
            /* Add data and delete listener to admin_manage_user.xml template  */
            final String id = service.getId();
            final String name = service.getName();
            serviceName.setText(service.getName());
            servicePrice.setText("$"+Double.toString(service.getPrice()));
            serviceRequiredDocuments.setText(service.getRequiredDocumentsString());
            serviceDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(Service.COLLECTION).document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Helper.snackbar(v, "Deleted: " + name);
                            adapter.services.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }

    }

    public ServiceManagerAdapter(Context context, ArrayList<Service> services) {
        this.context = context;
        this.setServices(services);
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* Recycler view method */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_manage_service_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceManagerAdapter.ViewHolder holder, int position) {
        /* Recycler view method - puts data into the layout */
        final Service service = this.services.get(position);
        holder.bind(service, position, this);
    }

    @Override
    public int getItemCount() {
        /* Recycler view method - get item count */
        return services.size();
    }
}
