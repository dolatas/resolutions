package com.dododev.resolutions;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dododev.resolutions.dao.ResolutionDao;
import com.dododev.resolutions.dao.impl.ResolutionDaoImpl;
import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.model.ResolutionStatusDict;
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
    private String originalStartDate, originalEndDate;

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

    @ViewById
    AdView adView;

    @ViewById
    Button delete;

    @ViewById
    ImageButton clearStartDate;

    @ViewById
    ImageButton clearEndDate;

    @AfterViews
    void initView() {
        Log.i("Resolution", "ResolutionForm > initView");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            resolution = (Resolution) bundle.getSerializable("resolution");

            if(resolution != null){
                title.setText(resolution.getTitle());
                if(resolution.getStartDate() != null) {
                    originalStartDate = SDF.format(resolution.getStartDate());
                    startDate.setText(originalStartDate);
                } else {
                    originalStartDate = "";
                }
                if(resolution.getEndDate() != null) {
                    originalEndDate = SDF.format(resolution.getEndDate());
                    endDate.setText(originalEndDate);
                } else {
                    originalEndDate = "";
                }
                delete.setVisibility(View.VISIBLE);
            }
        } else {
            resolution = null;
            originalStartDate = SDF.format(new Date());
            startDate.setText(originalStartDate);
            originalEndDate = "";
            delete.setVisibility(View.GONE);
        }

        startDateListener = new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                startDate.setText(SDF.format(c.getTime()));
                if(!originalStartDate.equals(SDF.format(c.getTime()))){
                    clearStartDate.setVisibility(View.VISIBLE);
                } else {
                    clearStartDate.setVisibility(View.GONE);
                }
            }
        };

        endDateListener = new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                endDate.setText(SDF.format(c.getTime()));
                if(!originalEndDate.equals(SDF.format(c.getTime()))){
                    clearEndDate.setVisibility(View.VISIBLE);
                } else {
                    clearEndDate.setVisibility(View.GONE);
                }
            }
        };

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Click
    void save(){
        if(title.getText().toString().equals("")){
            Toast.makeText(this, R.string.empty_title, Toast.LENGTH_LONG).show();
            return;
        }
        if(resolution == null){
            resolution = new Resolution();
        }
        resolution.setTitle(title.getText().toString());
        resolution.setDescription(description.getText().toString());
        try {
            Date start = null;
            if(!startDate.getText().toString().equals("")){
                start = SDF.parse(startDate.getText().toString());
            } else {
                start = new Date();
            }

            Date end = null;
            if(!endDate.getText().toString().equals("")) {
                end = SDF.parse(endDate.getText().toString());
            }

            if(end != null && end.before(start)){
                Toast.makeText(this, R.string.end_before_start, Toast.LENGTH_LONG).show();
                return;
            }

            resolution.setStartDate(start);
            resolution.setEndDate(end);

            if(start != null && new Date().before(start)){
                resolution.setStatus(ResolutionStatusDict.PENDING);
            } else if(end != null && new Date().after(end)) {
                resolution.setStatus(ResolutionStatusDict.UNKNOWN);
            } else {
                resolution.setStatus(ResolutionStatusDict.ONGOING);
            }

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

    @Click
    void clearStartDate(){
        startDate.setText(originalStartDate);
        clearStartDate.setVisibility(View.GONE);
    }

    @Click
    void clearEndDate(){
        endDate.setText(originalEndDate);
        clearEndDate.setVisibility(View.GONE);
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
