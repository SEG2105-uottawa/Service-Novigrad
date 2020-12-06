package com.example.novigrad.employee;

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

public class ViewCommentAdapter extends RecyclerView.Adapter<ViewCommentAdapter.ViewHolder>{
    private Context context;
    private ArrayList<String> comments;

    public ViewCommentAdapter(ArrayList<String> comments) {
        this.comments = comments;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView commentContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.commentContent = itemView.findViewById(R.id.CommentContent);
        }

        public void bind(final String comment) {
            commentContent.setText(comment);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_comments_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String comment = this.comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
