package com.dododev.resolutions.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.dododev.resolutions.model.Settings_;
import com.dododev.resolutions.services.SampleSchedulingService_;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class SampleAlarmReceiver extends WakefulBroadcastReceiver {
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("SampleAlarmReceiver", "onReceive");
        Intent service = new Intent(context, SampleSchedulingService_.class);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context) {
        Log.i("SampleAlarmReceiver", "setAlarm");

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SampleAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        SharedPreferences prefs = context.getSharedPreferences("SettingsActivity__Settings", Context.MODE_PRIVATE);
        int notificationTimeHour = prefs.getInt("notificationTimeHour", 12);
        int notificationTimeMinute = prefs.getInt("notificationTimeMinute", 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, notificationTimeMinute);
        calendar.set(Calendar.HOUR_OF_DAY, notificationTimeHour);

        alarmMgr. setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

//        alarmMgr. setRepeating(AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, Autostart_.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);           
    }
    // END_INCLUDE(set_alarm)

    /**
     * Cancels the alarm.
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
        
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, Autostart_.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(cancel_alarm)

    public boolean isAlarmOn(Context context) {
        return alarmMgr!= null;
    }

}
