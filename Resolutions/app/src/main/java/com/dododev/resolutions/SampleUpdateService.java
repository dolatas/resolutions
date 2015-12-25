package com.dododev.resolutions;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dododev.resolutions.dao.ResolutionDao;
import com.dododev.resolutions.dao.impl.ResolutionDaoImpl;
import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.model.ResolutionStatusDict;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;

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

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("SampleUpdateService", "onHandleIntent");
        // BEGIN_INCLUDE(service_onhandle)
        List<Resolution> resolutionList = resolutionDao.findAll();
        if(resolutionList != null && !resolutionList.isEmpty()){
            for(Resolution resolution : resolutionList){
                updateStatus(resolution);
            }
        }
        // Release the wake lock provided by the BroadcastReceiver.
        SampleAlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }

    private void updateStatus(Resolution resolution){

        ResolutionStatusDict newStatus = null;
        if(resolution.getStartDate() != null && new Date().before(resolution.getStartDate())){
            newStatus = ResolutionStatusDict.PENDING;
        } else if(resolution.getEndDate() != null && new Date().after(resolution.getEndDate())) {
            newStatus = ResolutionStatusDict.UNKNOWN;
        } else {
            newStatus = ResolutionStatusDict.ONGOING;
        }
        if(newStatus != null && resolution.getStatus()!= null && !resolution.getStatus().equals(newStatus)){
            resolutionDao.save(resolution);
        }
    }

}