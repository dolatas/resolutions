package com.dododev.resolutions;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dododev.resolutions.model.Resolution;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;

@EActivity(R.layout.activity_resolution_details)
public class ResolutionDetails extends Activity {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Resolution resolution;

    @ViewById
    TextView title;

    @ViewById
    TextView startDate;

    @ViewById
    TextView endDate;

    @AfterViews
    void initView() {
        Log.i("Resolutions", "ResolutionDetails > initView");

        //TODO
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
        }
    }
}
