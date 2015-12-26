package com.dododev.resolutions;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.dododev.resolutions.model.Resolution;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_resolutions)
@OptionsMenu(R.menu.menu_resolutions)
public class Resolutions extends AppCompatActivity {

    SampleAlarmReceiver alarm = new SampleAlarmReceiver();
    SampleUpdateReceiver update = new SampleUpdateReceiver();

    @ViewById
    ListView resolutionList;

    @ViewById
    FrameLayout addLayout;

    @ViewById
    AdView adView;

    @Bean
    ResolutionListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!alarm.isAlarmOn(this)){
            alarm.setAlarm(this);
        }
        if(!update.isAlarmOn(this)){
            update.setAlarm(this);
        }

    }

    @AfterViews
    void initView() {
        Log.i("Resolutions", "Resolutions > initView");
        resolutionList.setAdapter(adapter);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if(resolutionList.getCount() > 0){
            resolutionList.setVisibility(View.VISIBLE);
            addLayout.setVisibility(View.GONE);
        } else {
            addLayout.setVisibility(View.VISIBLE);
            resolutionList.setVisibility(View.GONE);
        }
    }

    @ItemClick
    void resolutionListItemClicked(Resolution resolution) {
        Intent intent = new Intent(this, ResolutionDetails_.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("resolution", resolution);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OptionsItem
    void action_add(MenuItem item) {
        Intent intent = new Intent(this, ResolutionForm_.class);
        startActivity(intent);

    }

//    @OptionsItem
//    void action_main(MenuItem item) {
//        Intent intent = new Intent(this, ResolutionList.class);
//        startActivity(intent);
//    }

    @Click
    void add(){
        Intent intent = new Intent(this, ResolutionForm_.class);
        startActivity(intent);
    }

     private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
