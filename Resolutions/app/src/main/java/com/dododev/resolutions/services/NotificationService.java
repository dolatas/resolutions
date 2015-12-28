package com.dododev.resolutions.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dododev.resolutions.R;
import com.dododev.resolutions.Resolutions_;
import com.dododev.resolutions.dao.ResolutionDao;
import com.dododev.resolutions.dao.impl.ResolutionDaoImpl;
import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.model.ResolutionStatusDict;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

import java.util.List;
import java.util.Random;

/**
 * Created by dodo on 2015-12-15.
 */
@EService
public class NotificationService extends Service {

    private static final String TAG = "NotificationService";

    @Bean(ResolutionDaoImpl.class)
    ResolutionDao resolutionDao;

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
        sendNotification();
        Log.d(TAG, "onStart");
        return 1;
    }

    private void sendNotification() {
//        List<Resolution> resolutionList = resolutionDao.findAll();
        List<Resolution> resolutionList = resolutionDao.findByType(ResolutionStatusDict.ONGOING);
        if(resolutionList != null && !resolutionList.isEmpty()){
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
                contentText = "oraz " + otherResolutionsNo + " inne postanowienie";
            } else if(otherResolutionsNo <= 4){
                contentText = "oraz " + otherResolutionsNo + " inne postanowienia";
            } else {
                contentText = "oraz " + otherResolutionsNo + " innych postanowień";
            }

//            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(resolution.getTitle())
                    .setContentText(contentText)
                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        }
    }
}
