package com.dododev.resolutions.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@EIntentService
public class SampleUpdateService extends IntentService {
    public SampleUpdateService() {
        super("UpdateService");
    }

    @Bean(ResolutionDaoImpl.class)
    ResolutionDao resolutionDao;

    @Pref
    Settings_ settings;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("SampleUpdateService", "onHandleIntent");
        // BEGIN_INCLUDE(service_onhandle)
        List<Resolution> resolutionList = resolutionDao.findAll();
        List<Resolution> newOngoingResolutions = new ArrayList<Resolution>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();
        if(resolutionList != null && !resolutionList.isEmpty()){
            for(Resolution resolution : resolutionList){
                if(updateStatus(resolution, today)){ //true if new ongoing
                    newOngoingResolutions.add(resolution);
                }
            }
        }

        if(newOngoingResolutions != null && !newOngoingResolutions.isEmpty()){
            sendNotification(newOngoingResolutions);
        }

        // Release the wake lock provided by the BroadcastReceiver.
        SampleAlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }

    private boolean updateStatus(Resolution resolution, Date today){

        ResolutionStatusDict newStatus = null;
        boolean newOngoing = false;
        if(resolution.getStartDate() != null && today.before(resolution.getStartDate())){
            newStatus = ResolutionStatusDict.PENDING;
        } else if(resolution.getEndDate() != null && today.after(resolution.getEndDate())) {
            newStatus = ResolutionStatusDict.UNKNOWN;
        } else {
            newStatus = ResolutionStatusDict.ONGOING;
        }
        if(newStatus != null && resolution.getStatus()!= null && !resolution.getStatus().equals(newStatus)){
            resolution.setStatus(newStatus);
            resolutionDao.save(resolution);
            if(newStatus.equals(ResolutionStatusDict.ONGOING)){
                newOngoing = true;
            }
        }

        return newOngoing;
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


        int newOngoingResolutionsNo = resolutionList.size();
        String contentText  = null;
        if(newOngoingResolutionsNo == 1){
            contentText = newOngoingResolutionsNo + " " + getString(R.string.one_new_ongoing);
        } else if(newOngoingResolutionsNo <= 4){
            contentText = newOngoingResolutionsNo + " " + getString(R.string.up_to_four_new_ongoing);
        } else {
            contentText = newOngoingResolutionsNo + " " + getString(R.string.more_than_four_new_ongoing);
        }

//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getText(R.string.do_you_remember))
                .setContentText(contentText)
                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());

    }

}