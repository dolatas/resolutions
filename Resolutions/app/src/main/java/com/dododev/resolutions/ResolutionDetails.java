package com.dododev.resolutions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.utils.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;

@EActivity(R.layout.activity_resolution_details)
public class ResolutionDetails extends Activity {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(Constants.DATE_FORMAT_DAY);
    private Resolution resolution;

    @ViewById
    TextView title;

    @ViewById
    TextView startDate;

    @ViewById
    TextView endDate;

    @ViewById
    AdView adView;

    @ViewById
    TextView status;

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
                    endDate.setVisibility(View.VISIBLE);
                } else {
                    endDate.setVisibility(View.GONE);
                }
                setStatus(resolution);
            }
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setStatus(Resolution resolution) {
//        Date current = new Date();
//        if(resolution.getStartDate().before(current)){
//
//        }

        switch(resolution.getStatus()){
            case PENDING:
                status.setText(getString(R.string.pending));
                status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pending, 0, 0, 0);
                break;
            case ONGOING:
                status.setText(getString(R.string.ongoing));
                status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ongoing, 0, 0, 0);
                break;
            case SUCCESS:
                status.setText(getString(R.string.success));
                status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.success, 0, 0, 0);
                break;
            case FAILURE:
                status.setText(getString(R.string.failure));
                status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.failure, 0, 0, 0);
                break;
            case UNKNOWN:
                status.setText(getString(R.string.unknown));
                status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unknown, 0, 0, 0);
                break;
        }
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
