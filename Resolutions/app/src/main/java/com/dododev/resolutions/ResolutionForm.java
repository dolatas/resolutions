package com.dododev.resolutions;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dododev.resolutions.dao.ResolutionDao;
import com.dododev.resolutions.dao.impl.ResolutionDaoImpl;
import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.utils.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@EActivity(R.layout.activity_resolution_form)
@OptionsMenu(R.menu.menu_resolution_form)
public class ResolutionForm extends Activity { // implements DatePickerDialog.OnDateSetListener {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(Constants.DATE_FORMAT_DAY);
    private Resolution resolution;
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;

    @Bean(ResolutionDaoImpl.class)
    ResolutionDao resolutionDao;

    @ViewById
    EditText title;

    @ViewById
    EditText description;

    @ViewById
    TextView startDate;

    @ViewById
    TextView endDate;

    @ViewById
    AdView adView;

    @ViewById
    Button delete;

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
                delete.setVisibility(View.VISIBLE);
            }
        } else {
            resolution = null;
            startDate.setText(SDF.format(new Date()));
            delete.setVisibility(View.GONE);
        }

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

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

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
            case R.id.startDateBtn:
                newFragment.setOnDateListener(startDateListener);
                break;
            case R.id.endDateBtn:
                newFragment.setOnDateListener(endDateListener);
                break;
        }
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
