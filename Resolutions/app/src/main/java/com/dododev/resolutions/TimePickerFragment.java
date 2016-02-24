package com.dododev.resolutions;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dodo on 2015-12-18.
 */
public class TimePickerFragment extends DialogFragment{

    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    int notificationTimeMinute = 0;
    int notificationTimeHour = 12;
    boolean is24HourFormat = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), onTimeSetListener, notificationTimeHour, notificationTimeMinute, is24HourFormat);
    }

    public void setOnTimeListener(TimePickerDialog.OnTimeSetListener onTimeSetListener, int notificationTimeHour, int notificationTimeMinute, boolean is24HourFormat) {
        this.onTimeSetListener = onTimeSetListener;
        this.is24HourFormat = is24HourFormat;
        this.notificationTimeHour = notificationTimeHour;
        this.notificationTimeMinute = notificationTimeMinute;
    }
}