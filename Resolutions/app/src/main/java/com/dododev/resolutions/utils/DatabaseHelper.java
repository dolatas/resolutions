package com.dododev.resolutions.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.androidannotations.annotations.EBean;

/**
 * Created by dodo on 2015-12-14.
 */
@EBean
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        setLocale(db);
        db.execSQL(Constants.RESOLUTION_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Constants.RESOLUTION_CREATE_TABLE);
        onCreate(db);

    }

    private void setLocale(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS \"android_metadata\"");
        db.execSQL("CREATE TABLE \"android_metadata\" (\"locale\" TEXT DEFAULT 'pl_PL')");
        db.execSQL("INSERT INTO \"android_metadata\" VALUES ('pl_PL')");
    }
}
