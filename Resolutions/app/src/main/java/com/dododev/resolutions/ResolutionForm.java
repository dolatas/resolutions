package com.dododev.resolutions;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.dododev.resolutions.dao.ResolutionDao;
import com.dododev.resolutions.dao.impl.ResolutionDaoImpl;
import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@EActivity(R.layout.activity_resolution_form)
public class ResolutionForm extends Activity { // implements DatePickerDialog.OnDateSetListener {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(Constants.DATE_FORMAT_DAY);
    private Resolution resolution;
    DatePickerDialog.OnDateSetListener startDateListener,endDateListener;
    int from_year, from_month, from_day,to_year, to_month, to_day; //initialize them to current date in onStart()/onCreate()

    final int DATE_PICKER_TO = 0;
    final int DATE_PICKER_FROM = 1;

    @Bean(ResolutionDaoImpl.class)
    ResolutionDao resolutionDao;

    @ViewById
    EditText title;

    @ViewById
    EditText description;

    @ViewById
    EditText startDate;

    @ViewById
    EditText endDate;

    @AfterViews
    void initView() {
        Log.i("Resolution", "ResolutionForm > initView");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            resolution = (Resolution) bundle.getSerializable("resolution");

            if(resolution != null){
                title.setText(resolution.getTitle());
                if(resolution.getStartDate() != null) {
                    startDate.setText(SDF.format(resolution.getStartDate()));
                }
                if(resolution.getEndDate() != null) {
                    endDate.setText(SDF.format(resolution.getEndDate()));
                }
            }
        } else {
            resolution = null;
        }


        Calendar calendar = Calendar.getInstance();
        from_year = calendar.getGreatestMinimum(Calendar.YEAR);
        from_month = calendar.getGreatestMinimum(Calendar.MONTH);
        from_day = calendar.getGreatestMinimum(Calendar.DAY_OF_MONTH);
        to_year = calendar.getGreatestMinimum(Calendar.YEAR);
        to_month = calendar.getGreatestMinimum(Calendar.MONTH);
        to_day = calendar.getGreatestMinimum(Calendar.DAY_OF_MONTH);



        startDateListener = new DatePickerDialog.OnDateSetListener(){

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                startDate.setText(SDF.format(c.getTime()));
            }
        };

        endDateListener = new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                endDate.setText(SDF.format(c.getTime()));
            }
        };

    }

    @Click
    void save(){
        if(resolution == null){
            resolution = new Resolution();
        }
        resolution.setTitle(title.getText().toString());
        resolution.setDescription(description.getText().toString());
        try {
            resolution.setStartDate(SDF.parse(startDate.getText().toString()));
            resolution.setEndDate(SDF.parse(endDate.getText().toString()));
        } catch (ParseException e) {
            Log.e("Date parsing error", e.getMessage());
        }

        resolutionDao.save(resolution);

        Intent intent = new Intent(this, Resolutions_.class);
        startActivity(intent);
        Toast.makeText(this, getString(R.string.resolution_saved), Toast.LENGTH_SHORT).show();
    }

    @Click
    void cancel(){
        Intent intent = new Intent(this, Resolutions_.class);
        startActivity(intent);
    }

    @Click
    void delete(){
        resolutionDao.delete(resolution);
        Intent intent = new Intent(this, Resolutions_.class);
        startActivity(intent);
        Toast.makeText(this, getString(R.string.resolution_deleted), Toast.LENGTH_SHORT).show();
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        switch (v.getId()){
            case R.id.startDate:
                newFragment.setOnDateListener(startDateListener);
                break;
            case R.id.endDate:
                newFragment.setOnDateListener(endDateListener);
                break;
        }
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
