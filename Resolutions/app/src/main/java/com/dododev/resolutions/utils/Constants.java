package com.dododev.resolutions.utils;

/**
 * Created by dodo on 2015-12-14.
 */
public class Constants {

    public static final String DATE_FORMT_DAY = "yyyy-MM-dd";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "resolutions.db";

    // TABLE_RESOLUTION
    public static final String TABLE_RESOLUTION = "tb_resolution";
    public static final String R_ID = "_id";
    public static final String R_TITLE = "title";
    public static final String R_DESCRIPTION = "description";
    public static final String R_START_DATE = "startDate";
    public static final String R_END_DATE = "end_date";
    public static final String R_STATUS = "status";


    // tables creation sql statements
    public static final String RESOLUTION_CREATE_TABLE = "create table "
            + TABLE_RESOLUTION + "("
            + R_ID + " integer primary key autoincrement, "
            + R_TITLE + " text, "
            + R_DESCRIPTION + " text, "
            + R_START_DATE + " text, "
            + R_END_DATE + " text, "
            + R_STATUS + " text " + "); ";

}
