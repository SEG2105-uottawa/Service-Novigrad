package com.example.novigrad;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public abstract class Helper {
    public static void snackbar(View view, String message) {
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
