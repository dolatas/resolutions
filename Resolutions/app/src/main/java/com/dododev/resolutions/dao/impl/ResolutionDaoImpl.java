package com.dododev.resolutions.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dododev.resolutions.dao.ResolutionDao;
import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.utils.Constants;
import com.dododev.resolutions.utils.DatabaseHelper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dodo on 2015-12-14.
 */
@EBean
public class ResolutionDaoImpl implements ResolutionDao {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(Constants.DATE_FORMAT_DAY);
    SQLiteDatabase db;

    @Bean
    DatabaseHelper dbHelper;

    @RootContext
    Context context;

    @AfterInject
    void initHelper() {
        Log.i("Resolutions", "ResolutionDaoImpl > initHelper");
        if (dbHelper == null) {
            Log.i("Resolutions", "ResolutionDaoImpl > initHelper > dbHelper init");
            dbHelper = new DatabaseHelper(context);
        }
        if (db == null) {
            db = dbHelper.getWritableDatabase();
            Log.i("Resolutions", "ResolutionDaoImpl > initHelper > db init");
        }
    }

    @Override
    public List<Resolution> findAll() {
        String sortOrder = Constants.R_END_DATE + " DESC";

        Cursor cursor = db.query(
            Constants.TABLE_RESOLUTION,  // The table to query
            null,                        // The columns to return
            null,                        // The columns for the WHERE clause
            null,                        // The values for the WHERE clause
            null,                        // don't group the rows
            null,                        // don't filter by row groups
            sortOrder                    // The sort order
        );


        List<Resolution> list = new ArrayList<Resolution>();
        if (cursor.moveToFirst()) {
            do {

                Resolution resolution = new Resolution();
                resolution.setId(cursor.getLong(cursor.getColumnIndex(Constants.R_ID)));
                resolution.setTitle(cursor.getString(cursor.getColumnIndex(Constants.R_TITLE)));
                resolution.setDescription(cursor.getString(cursor.getColumnIndex(Constants.R_DESCRIPTION)));
                try {
                    String dateHelper = cursor.getString(cursor.getColumnIndex(Constants.R_START_DATE));
                    if(dateHelper != null && !dateHelper.isEmpty()){
                        resolution.setStartDate(SDF.parse(dateHelper));
                    }
                    dateHelper = cursor.getString(cursor.getColumnIndex(Constants.R_END_DATE));
                    if(dateHelper != null && !dateHelper.isEmpty()){
                        resolution.setEndDate(SDF.parse(dateHelper));
                    }
                } catch (ParseException e) {
                    Log.e("Data parsing error", e.getMessage());
                }

                list.add(resolution);

            } while (cursor.moveToNext());
        }

        return list;
    }

    @Override
    public Resolution getById(Long id) {
        //TODO
        Resolution r = new Resolution();
        r.setTitle("Postanowienie " + id);
        r.setStartDate(new Date());
        r.setEndDate(new Date());
        return r;
    }

    @Override
    public Long save(Resolution resolution) {
        ContentValues values = new ContentValues();
        values.put(Constants.R_TITLE, resolution.getTitle());
        values.put(Constants.R_DESCRIPTION, resolution.getDescription());
        if(resolution.getStartDate() != null){
            values.put(Constants.R_START_DATE, SDF.format(resolution.getStartDate()));
        }
        if(resolution.getEndDate() != null) {
            values.put(Constants.R_END_DATE, SDF.format(resolution.getEndDate()));
        }

        boolean isNew = resolution.getId() == null;
        long rowId;
        if(!isNew){
            rowId = resolution.getId();
            String selection = Constants.R_ID + " = ?";
            String[] selectionArgs = { String.valueOf(rowId) };

            int count = db.update(
                    Constants.TABLE_RESOLUTION,
                    values,
                    selection,
                    selectionArgs);
        } else {
            rowId = db.insert(
                    Constants.TABLE_RESOLUTION,
                    null,
                    values);

        }
        return rowId;
    }

    @Override
    public void delete(Resolution resolution) {
        if(resolution.getId() != null){
            String selection = Constants.R_ID + " = ?";
            String[] selectionArgs = { String.valueOf(resolution.getId()) };
            db.delete(Constants.TABLE_RESOLUTION, selection, selectionArgs);
        }
    }
}
