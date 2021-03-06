package com.example.novigrad.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.domain.Service;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ServiceManagerAdapter extends RecyclerView.Adapter<ServiceManagerAdapter.ViewHolder> {
    /* Adapter to create the admin service manager */
    private Context context;
    private ArrayList<Service> services = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView serviceName;
        private TextView servicePrice;
        private TextView serviceRequiredDocuments;
        private Button serviceDelete, serviceEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceNameTextView);
            servicePrice = itemView.findViewById(R.id.servicePriceTextView);
            serviceRequiredDocuments = itemView.findViewById(R.id.serviceRequiredDocumentsTextView);
            serviceDelete = itemView.findViewById(R.id.serviceDeleteButton);
            serviceEdit = itemView.findViewById(R.id.serviceEditButton);
        }

        public void bind(final Service service, final int position, final ServiceManagerAdapter adapter) {
            /* Add data and delete listener to admin_manage_user.xml template  */
            final String id = service.getId();
            final String name = service.getName();
            serviceName.setText(service.getName());
            servicePrice.setText(String.format("$ %.2f", service.getPrice()));
            serviceRequiredDocuments.setText(service.getRequiredDocumentsString());
            serviceDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    adapter.services.remove(getAdapterPosition());
                    adapter.notifyDataSetChanged();

                    String url = "https://us-central1-novigrad-eadd2.cloudfunctions.net/novigradAdminDeleteService?id="+id;
                    Volley.newRequestQueue(adapter.context).add(new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Helper.snackbar(v, "Deleted: " + service.getName());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                        }
                    }));
                }
            });
            serviceEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent intent = new Intent(v.getContext(), ServiceEditorActivity.class);
                    intent.putExtra("id", service.getId());
                    v.getContext().startActivity(intent);
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
