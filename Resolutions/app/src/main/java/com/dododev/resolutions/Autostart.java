package com.dododev.resolutions;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.androidannotations.annotations.EReceiver;

/**
 * Created by dodo on 2015-12-15.
 */
@EReceiver
public class Autostart extends BroadcastReceiver {
    SampleAlarmReceiver alarm = new SampleAlarmReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") ||
            intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")){
            alarm.setAlarm(context);
        }

//        Intent i = new Intent(context, NotificationService_.class);
//        context.startService(i);


        Log.i("Autostart", "started");
    }
}
