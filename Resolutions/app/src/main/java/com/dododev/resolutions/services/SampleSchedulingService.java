package com.dododev.resolutions.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dododev.resolutions.R;
import com.dododev.resolutions.Resolutions_;
import com.dododev.resolutions.dao.ResolutionDao;
import com.dododev.resolutions.dao.impl.ResolutionDaoImpl;
import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.model.ResolutionStatusDict;
import com.dododev.resolutions.model.Settings_;
import com.dododev.resolutions.receivers.SampleAlarmReceiver;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;
import java.util.Random;

@EIntentService
public class SampleSchedulingService extends IntentService {
    public SampleSchedulingService() {
        super("SchedulingService");
    }

    @Bean(ResolutionDaoImpl.class)
    ResolutionDao resolutionDao;

    @Pref
    Settings_ settings;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("SampleSchedulingService", "onHandleIntent");
        // BEGIN_INCLUDE(service_onhandle)
        List<Resolution> resolutionList = resolutionDao.findByType(ResolutionStatusDict.ONGOING);
        if(resolutionList != null && !resolutionList.isEmpty()){
            sendNotification(resolutionList);
        }
        // Release the wake lock provided by the BroadcastReceiver.
        SampleAlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }

    private void sendNotification(List<Resolution> resolutionList) {

            Random rand = new Random();
            //int randomNum = rand.nextInt((max - min) + 1) + min;
            int randomNum = rand.nextInt((resolutionList.size() - 1 - 0) + 1) + 0;
            Resolution resolution = resolutionList.get(randomNum);

            Intent intent = new Intent(this, Resolutions_.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);


            int otherResolutionsNo = resolutionList.size() - 1;
            String contentText  = null;
            if(otherResolutionsNo == 0){
                contentText = resolution.getDescription();
            } else if(otherResolutionsNo == 1){
                contentText = getString(R.string.and) + " " + otherResolutionsNo + " " + getString(R.string.one_other);
            } else if(otherResolutionsNo <= 4){
                contentText = getString(R.string.and) + " " + otherResolutionsNo + " " + getString(R.string.up_to_four_others);
            } else {
                contentText = getString(R.string.and) + " " + otherResolutionsNo + " " + getString(R.string.more_than_four_others);
            }

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(resolution.getTitle())
                    .setContentText(contentText)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

}