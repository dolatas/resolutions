package com.dododev.resolutions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@EActivity(R.layout.activity_resolution_form)
public class ResolutionForm extends Activity {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(Constants.DATE_FORMT_DAY);
    private Resolution resolution;

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

    }

    @Click
    void save(){
        resolution = new Resolution();
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
        Toast.makeText(this, getString(R.string.resolution_added), Toast.LENGTH_SHORT).show();
    }
}
