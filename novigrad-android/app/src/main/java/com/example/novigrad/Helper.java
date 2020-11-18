package com.example.novigrad;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public abstract class Helper {
    public static void snackbar(View view, String message) {
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static String getText(TextInputLayout input) {
        try { return input.getEditText().getText().toString(); }
        catch (Exception e) {
            return null;
        }
    }

    public static String getText(EditText input) {
        try { return input.getText().toString(); }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean stringIsValid(String s) {
        return (s != null) && (s.length() > 0);
    }
    public static String getSelectedRadioText(RadioGroup input) {
        /* Get the selected role - Either 'Customer' or 'Employee' */
        try {
            RadioButton selected = input.findViewById(input.getCheckedRadioButtonId());
            return selected.getText().toString();
        } catch (Exception e) {
            return null;
        }
    }
}
