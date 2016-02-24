package com.dododev.resolutions;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dododev.resolutions.model.Settings_;
import com.dododev.resolutions.receivers.SampleAlarmReceiver;
import com.dododev.resolutions.utils.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends Activity {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(Constants.DATE_FORMAT_DAY);
    private TimePickerDialog.OnTimeSetListener notificationTimeListener;
    private boolean is24HourFormat = true;
    private int notificationTimeHour;
    private int notificationTimeMinute;

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
    EditText notificationTime;

    @ViewById
    AdView adView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @AfterViews
    void initView() {
        Log.i("Resolutions", "SettingsActivity > initView");

        is24HourFormat = DateFormat.is24HourFormat(getApplicationContext());
        playNotificationSound.setChecked(settings.playNotificationSound().get());
        notificationTimeHour = settings.notificationTimeHour().get();
        notificationTimeMinute = settings.notificationTimeMinute().get();
        setNotificationTimeText(notificationTimeHour, notificationTimeMinute);

        notificationTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                notificationTimeHour = hourOfDay;
                notificationTimeMinute = minute;
                setNotificationTimeText(notificationTimeHour, notificationTimeMinute);
            }
        };

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


    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        switch (v.getId()) {
            case R.id.notificationTime:
                newFragment.setOnTimeListener(notificationTimeListener, notificationTimeHour, notificationTimeMinute, is24HourFormat);
                break;
        }
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Click
    void save() {
        boolean resetAlarm = notificationTimeHour != settings.notificationTimeHour().get() || notificationTimeMinute != settings.notificationTimeMinute().get();

        settings.edit()
                .playNotificationSound().put(playNotificationSound.isChecked())
                .notificationTimeHour().put(notificationTimeHour)
                .notificationTimeMinute().put(notificationTimeMinute)
                .apply();

        Intent intent = new Intent(this, Resolutions_.class);
        intent.putExtra("resetAlarm", resetAlarm);
        startActivity(intent);
        Toast.makeText(this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
    }

    @Click
    void cancel() {
        Intent intent = new Intent(this, Resolutions_.class);
        startActivity(intent);
    }

    private void setNotificationTimeText(int hourOfDay, int minute){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, notificationTimeHour);
        c.set(Calendar.MINUTE, notificationTimeMinute);
        if (is24HourFormat) {
            notificationTime.setText(DateFormat.format("HH:mm", c));
        } else{
            notificationTime.setText(DateFormat.format("hh:mm a", c));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Settings Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.dododev.resolutions/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Settings Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.dododev.resolutions/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
