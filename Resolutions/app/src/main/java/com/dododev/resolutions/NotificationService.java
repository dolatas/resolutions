package com.dododev.resolutions;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.dododev.resolutions.model.Resolution;

import org.androidannotations.annotations.EService;

/**
 * Created by dodo on 2015-12-15.
 */
@EService
public class NotificationService extends Service {

    private static final String TAG = "NotificationService";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Intent intents = new Intent(getBaseContext(), Resolutions_.class);
        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intents);
//        Toast.makeText(this, "NotificationService Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");
        return 1;
    }
}
