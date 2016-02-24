package com.dododev.resolutions.model;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

import java.util.Date;
import java.util.List;

/**
 * Created by sdolata on 2015-12-28.
 */
@SharedPref
public interface Settings {
    @DefaultBoolean(true)
    boolean playNotificationSound();

    @DefaultBoolean(true)
    boolean displayOngoing();

    @DefaultBoolean(true)
    boolean displayPending();

    @DefaultBoolean(true)
    boolean displayUnknown();

    @DefaultBoolean(false)
    boolean displaySuccess();

    @DefaultBoolean(false)
    boolean displayFailure();

    @DefaultInt(12)
    int notificationTimeHour();

    @DefaultInt(0)
    int notificationTimeMinute();

}
