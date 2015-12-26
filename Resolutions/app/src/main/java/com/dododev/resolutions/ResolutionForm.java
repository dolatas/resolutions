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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@EActivity(R.layout.activity_resolution_form)
//@OptionsMenu(R.menu.menu_resolution_form)
public class ResolutionForm extends Activity { // implements DatePickerDialog.OnDateSetListener {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(Constants.DATE_FORMAT_DAY);
    private Resolution resolution;
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;
    private Date originalStartDate, originalEndDate;

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

    @ViewById
    ImageButton emptyEndDate;

    @AfterViews
    void initView() {
        Log.i("Resolution", "ResolutionForm > initView");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            resolution = (Resolution) bundle.getSerializable("resolution");

            if (resolution != null) {
                title.setText(resolution.getTitle());
                description.setText(resolution.getDescription());
                if (resolution.getStartDate() != null) {
                    originalStartDate = resolution.getStartDate();
                    startDate.setText(SDF.format(originalStartDate));
                } else {
                    originalStartDate =  new Date();
                    startDate.setText(SDF.format(new Date()));
                }
                if (resolution.getEndDate() != null) {
                    originalEndDate = resolution.getEndDate();
                    endDate.setText(SDF.format(originalEndDate));
                    emptyEndDate.setVisibility(View.VISIBLE);
                } else {
                    originalEndDate = null;
                    endDate.setText("");
                    emptyEndDate.setVisibility(View.GONE);
                }
                delete.setVisibility(View.VISIBLE);
            }
        } else {
            resolution = null;
            originalStartDate = new Date();
            startDate.setText(SDF.format(new Date()));
            originalEndDate = null;
            delete.setVisibility(View.GONE);
            emptyEndDate.setVisibility(View.GONE);
        }

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            final Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth);
            startDate.setText(SDF.format(c.getTime()));
            if (originalStartDate == null || !originalStartDate.equals(c.getTime())) {
                clearStartDate.setVisibility(View.VISIBLE);
            } else {
                clearStartDate.setVisibility(View.GONE);
            }
            resolution.setStartDate(c.getTime());
            }
        };

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            final Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth);
            endDate.setText(SDF.format(c.getTime()));
            if (originalEndDate == null){
                emptyEndDate.setVisibility(View.VISIBLE);
                clearEndDate.setVisibility(View.GONE);
            } else if(!originalEndDate.equals(c.getTime())) {
                clearEndDate.setVisibility(View.VISIBLE);
            } else {
                emptyEndDate.setVisibility(View.VISIBLE);
                clearEndDate.setVisibility(View.GONE);
            }
            resolution.setEndDate(c.getTime());
            }
        };

        if (resolution == null) {
            resolution = new Resolution();
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Click
    void save() {
        if (title.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.empty_title), Toast.LENGTH_LONG).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();

        if (resolution.getStartDate() == null) {
            resolution.setStartDate(today);
        }

        if (resolution.getEndDate() != null && resolution.getEndDate().before(resolution.getStartDate())) {
            Toast.makeText(this, getString(R.string.end_before_start), Toast.LENGTH_LONG).show();
            return;
        }

        resolution.setTitle(title.getText().toString());
        resolution.setDescription(description.getText().toString());

        if (resolution.getStartDate() != null && today.before(resolution.getStartDate())) {
            resolution.setStatus(ResolutionStatusDict.PENDING);
        } else if (resolution.getEndDate() != null && today.after(resolution.getEndDate())) {
            resolution.setStatus(ResolutionStatusDict.UNKNOWN);
        } else {
            resolution.setStatus(ResolutionStatusDict.ONGOING);
        }


        resolutionDao.save(resolution);

        Intent intent = new Intent(this, Resolutions_.class);
        startActivity(intent);
        Toast.makeText(this, getString(R.string.resolution_saved), Toast.LENGTH_SHORT).show();
    }


    @Click
    void cancel() {
        Intent intent = new Intent(this, Resolutions_.class);
        startActivity(intent);
    }

    @Click
    void delete() {
        resolutionDao.delete(resolution);
        Intent intent = new Intent(this, Resolutions_.class);
        startActivity(intent);
        Toast.makeText(this, getString(R.string.resolution_deleted), Toast.LENGTH_SHORT).show();
    }

    @Click
    void clearStartDate() {
        startDate.setText(SDF.format(originalStartDate));
        resolution.setStartDate(originalStartDate);
        clearStartDate.setVisibility(View.GONE);
    }

    @Click
    void clearEndDate() {
        endDate.setText(SDF.format(originalEndDate));
        resolution.setEndDate(originalEndDate);
        clearEndDate.setVisibility(View.GONE);
        if(originalEndDate != null){
            emptyEndDate.setVisibility(View.VISIBLE);
        } else {
            emptyEndDate.setVisibility(View.GONE);
        }
    }

    @Click
    void emptyEndDate() {
        resolution.setEndDate(null);
        endDate.setText("");
        emptyEndDate.setVisibility(View.GONE);
        if(originalEndDate != null){
            clearEndDate.setVisibility(View.VISIBLE);
        }
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        switch (v.getId()) {
            case R.id.startDate:
                newFragment.setOnDateListener(startDateListener, resolution.getStartDate());
                break;
            case R.id.endDate:
                newFragment.setOnDateListener(endDateListener, resolution.getEndDate());
                break;
        }
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
