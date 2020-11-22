package com.example.novigrad.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.novigrad.Helper;
import com.example.novigrad.R;
import com.example.novigrad.validation.ProfileData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.ArrayList;

public class ProfileEditorActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    public EditText streetNumber, streetName, postalCode, municipality, phone;
    public TextView fromTime, toTime;
    public CheckBox[] days;
    public OnTimeSetListener fromTimeListener, toTimeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor_activity);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        streetNumber = findViewById(R.id.streetNumEditText);
        streetName = findViewById(R.id.streetNameEditText);
        postalCode = findViewById(R.id.postalEditText);
        municipality = findViewById(R.id.municipalityEditText);
        phone = findViewById(R.id.phoneEditText);
        fromTime = findViewById(R.id.fromTimeTextView);
        toTime = findViewById(R.id.toTimeTextView);

        fromTimeListener = new OnTimeSetListener(fromTime, 8, 30);
        toTimeListener = new OnTimeSetListener(toTime, 16, 0);
        createTimePicker(R.id.fromBtn, fromTimeListener);
        createTimePicker(R.id.toBtn, toTimeListener);

        days = new CheckBox[]{
                findViewById(R.id.sunday), findViewById(R.id.monday), findViewById(R.id.tuesday), findViewById(R.id.wednesday),
                findViewById(R.id.thursday), findViewById(R.id.friday), findViewById(R.id.saturday)
        };
    }

    public void onSaveProfile(final View view) {
        ProfileData profileData = new ProfileData(this);
        if (profileData.isValid(view)) {
            db.collection("users").document(mAuth.getUid()).update("profile", profileData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Helper.snackbar(view, getResources().getString(R.string.profile_save_success));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Helper.snackbar(view, getResources().getString(R.string.profile_save_failed));
                }
            });
        }
    }

    public void createTimePicker(int btn, final OnTimeSetListener listener) {
        Button timeBtn = findViewById(btn);
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ProfileEditorActivity.this, listener, listener.h, listener.m, false);
                timePickerDialog.show();
            }
        });
    }

    public ArrayList<String> getCheckedDays() {
        ArrayList<String> checkedDays = new ArrayList<>();
        for (CheckBox day: days) {
            if (day.isChecked()) {
                checkedDays.add((String) day.getText());
            }
        }
        return checkedDays;
    }
}