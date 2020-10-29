package com.example.novigrad.admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.novigrad.R;
import com.example.novigrad.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageUsers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageUsers extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            // Get all users
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot result = task.getResult();
                if (task.isSuccessful() && result != null) {
                    List<DocumentSnapshot> documents = result.getDocuments();
                    ArrayList<User> users = new ArrayList<>();
                    for (DocumentSnapshot document: documents) {
                        users.add(new User(document));
                    }
                    createUserManager(users, R.id.UserManagerRecyclerView, getActivity().getApplicationContext());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_users, container, false);
    }

    private UserManagerAdapter createUserManager(ArrayList<User> users, int recyclerId, Context context) {
        /* Create a user manager adapter*/
        RecyclerView usersRecycler = getView().findViewById(recyclerId);
        UserManagerAdapter adapter = new UserManagerAdapter(context, users);
        usersRecycler.setAdapter(adapter);
        usersRecycler.setLayoutManager(new LinearLayoutManager(context));
        return adapter;
    }
}