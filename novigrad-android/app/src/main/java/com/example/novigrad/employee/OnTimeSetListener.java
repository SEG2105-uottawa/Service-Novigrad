package com.example.novigrad.employee;

import android.app.TimePickerDialog;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.novigrad.Helper;

public class OnTimeSetListener implements TimePickerDialog.OnTimeSetListener {
    public int h, m;
    public TextView output;

    public OnTimeSetListener(TextView output, int defaultHour, int defaultMinute) {
        this.h = defaultHour;
        this.m = defaultMinute;
        this.output = output;
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.h = hourOfDay;
        this.m = minute;
        int value = (hourOfDay*60) + minute;
        output.setText(Helper.convertTimeToString(value));
    }

    public int getTime() {
        return (60*h) + m;
    }
}
