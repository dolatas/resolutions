package com.dododev.resolutions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dododev.resolutions.dao.ResolutionDao;
import com.dododev.resolutions.dao.impl.ResolutionDaoImpl;
import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.model.Settings;
import com.dododev.resolutions.model.Settings_;
import com.dododev.resolutions.utils.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.SimpleDateFormat;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends Activity {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(Constants.DATE_FORMAT_DAY);

    @Pref
    Settings_ settings;

    @ViewById
    TextView ongoing;

    @ViewById
    TextView pending;

    @ViewById
    TextView unknown;

    @ViewById
    Switch playNotificationSound;

    @ViewById
    AdView adView;

    @AfterViews
    void initView() {
        Log.i("Resolutions", "SettingsActivity > initView");

        //TODO load settings
        playNotificationSound.setChecked(settings.playNotificationSound().get());


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setStatusVisibility(TextView status, int stringId, int drawableId) {
        status.setText(getString(stringId));
        status.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);

//        switch(resolution.getStatus()){
//            case PENDING:
//
//                break;
//            case ONGOING:
//                status.setText(getString(R.string.ongoing));
//                status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ongoing, 0, 0, 0);
//                break;
//            case SUCCESS:
//                status.setText(getString(R.string.success));
//                status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.success, 0, 0, 0);
//                break;
//            case FAILURE:
//                status.setText(getString(R.string.failure));
//                status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.failure, 0, 0, 0);
//                break;
//            case UNKNOWN:
//                status.setText(getString(R.string.unknown));
//                status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unknown, 0, 0, 0);
//                break;
//        }
    }


    void save(){
        //TODO: save
        settings.edit()
                .playNotificationSound().put(playNotificationSound.isChecked());

        Intent intent = new Intent(this, Resolutions_.class);
        startActivity(intent);
    }

    void cancel(){
        Intent intent = new Intent(this, Resolutions_.class);
        startActivity(intent);
    }


}
