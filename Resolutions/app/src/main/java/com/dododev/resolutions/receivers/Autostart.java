package com.dododev.resolutions.receivers;

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
    SampleUpdateReceiver update = new SampleUpdateReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") ||
            intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")){
            alarm.setAlarm(context);
            update.setAlarm(context);
        }

        Log.i("Autostart", "started");
    }
}
