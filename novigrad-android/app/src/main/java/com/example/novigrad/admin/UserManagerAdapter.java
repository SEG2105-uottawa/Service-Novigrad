package com.example.novigrad.admin;

import android.content.Context;
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
import com.example.novigrad.domain.User;

import java.util.ArrayList;

public class UserManagerAdapter extends RecyclerView.Adapter<UserManagerAdapter.ViewHolder> {
    /* Adapter to create the admin user manager */
    private Context context;
    private ArrayList<User> users = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userEmail, userName, userRole;
        private Button userDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.userEmailTextView);
            userName = itemView.findViewById(R.id.userNameTextView);
            userRole = itemView.findViewById(R.id.userRoleTextView);
            userDelete = itemView.findViewById(R.id.userDeleteButton);
        }

        public void bind(User user, final UserManagerAdapter adapter) {
            /* Add data and delete listener to admin_manage_user.xml template  */
            final String id = user.getId();
            final String email = user.getEmail();
            userEmail.setText(user.getEmail());
            userRole.setText(user.getRole());
            userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));

            userDelete.setOnClickListener(new View.OnClickListener() {
                // Delete a user by uid from Firestore and Firebase auth
                @Override
                public void onClick(final View v) {
                    // This triggers a function to delete the user in firestore and firebase auth
                    adapter.users.remove(getAdapterPosition());
                    adapter.notifyDataSetChanged();

                    String url = "https://us-central1-novigrad-eadd2.cloudfunctions.net/novigradAdminDeleteUser?id="+id;
                    Volley.newRequestQueue(adapter.context).add(new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Helper.snackbar(v, "Deleted: " + email);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                        }
                    }));
                }
            });
        }
    }

    public UserManagerAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.setUsers(users);
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* Recycler view method */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_manage_user_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserManagerAdapter.ViewHolder holder, int position) {
        /* Recycler view method - puts data into the layout */
        final User user = this.users.get(position);
        holder.bind(user, this);
    }

    @Override
    public int getItemCount() {
        /* Recycler view method - get item count */
        return users.size();
    }
}
