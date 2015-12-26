package com.dododev.resolutions;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dodo on 2015-12-18.
 */
public class DatePickerFragment extends DialogFragment{

    DatePickerDialog.OnDateSetListener onDateSetListener;
    Date orgDate = new Date();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        if(orgDate != null) {
            c.setTime(orgDate);
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);
    }

    public void setOnDateListener(DatePickerDialog.OnDateSetListener onDateSetListener, Date orgDate) {
        this.onDateSetListener = onDateSetListener;
        this.orgDate = orgDate;
    }
}