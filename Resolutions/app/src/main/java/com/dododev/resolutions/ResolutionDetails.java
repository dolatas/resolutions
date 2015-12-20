package com.dododev.resolutions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dododev.resolutions.model.Resolution;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
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

    @ViewById
    AdView adView;

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

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Click
    void edit(){
        Intent intent = new Intent(this, ResolutionForm_.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("resolution", resolution);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
