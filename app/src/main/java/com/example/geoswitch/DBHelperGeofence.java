package com.example.geoswitch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelperGeofence extends SQLiteOpenHelper {

    public static final String DBNAME = "geoswitch.db";
    String email = getEmail();

    public DBHelperGeofence(Context context) {
        super(context, DBNAME, null, Integer.parseInt(context.getString(R.string.db_version)));
    }

    private String getEmail() {
        if (Global.getUserType() == Global.USER) {
            return Global.getCurrentUser().getEmail();
        } else {
            return Global.getBusinessOwner().getEmail();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //GEOFENCE TABLE OPERATIONS

    public int addGeofence(GeofenceCoordinates geofenceCoordinates) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("u_email", email);
        contentValues.put("latitude", geofenceCoordinates.getLatitude());
        contentValues.put("longitude", geofenceCoordinates.getLongitude());
        contentValues.put("radius", geofenceCoordinates.getRadius());

        int gid = (int) db.insert("geofences", null, contentValues);
        Log.d("geofences", String.valueOf(gid));
        return gid;
    }

    public GeofenceCoordinates getGeofence(int gid) {
        SQLiteDatabase mydb = this.getReadableDatabase();
        GeofenceCoordinates geofenceCoordinates = null;

        Log.d("testing", email + gid);
        Cursor cursor = mydb.rawQuery("SELECT latitude, longitude, radius FROM geofences WHERE u_email=? AND gid =?",
                new String[]{email, String.valueOf(gid)}, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                double latitude = cursor.getDouble(0);
                Log.d("testing", "lat" + latitude);
                double longitude = cursor.getDouble(1);
                Log.d("testing", "lon" + longitude);
                float radius = cursor.getFloat(2);

                geofenceCoordinates = new GeofenceCoordinates(gid, latitude, longitude, radius);
            }
        }
        return geofenceCoordinates;
    }

    public GeofenceCoordinates getGeofence(String boEmail, int gid) {
        SQLiteDatabase mydb = this.getReadableDatabase();
        GeofenceCoordinates geofenceCoordinates = null;

        Log.d("testing", email + gid);
        Cursor cursor = mydb.rawQuery("SELECT latitude, longitude, radius FROM geofences WHERE u_email=? AND gid =?",
                new String[]{boEmail, String.valueOf(gid)}, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                double latitude = cursor.getDouble(0);
                Log.d("testing", "lat" + latitude);
                double longitude = cursor.getDouble(1);
                Log.d("testing", "lon" + longitude);
                float radius = cursor.getFloat(2);

                geofenceCoordinates = new GeofenceCoordinates(gid, latitude, longitude, radius);
            }
        }
        return geofenceCoordinates;
    }


    //REMINDER TABLE OPERATIONS

    public int addReminder(Reminder reminder) {
        int gid = addGeofence(reminder.geofenceCoordinates);

        if (gid == -1) {
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("u_email", email);
        contentValues.put("gid", gid);
        contentValues.put("task", reminder.getTask());
        contentValues.put("details", reminder.getDetails());
        contentValues.put("start_date", reminder.getStartDate());
        contentValues.put("end_date", reminder.getEndDate());

        long result = db.insert("reminders", null, contentValues);
        Log.d("reminder", String.valueOf(result));


        if (result == -1) {
            return -1;
        }

        Global.reminderGid = getReminderGidList();
        return gid;
    }


    public ArrayList<Reminder> getReminders() {
        ArrayList<Reminder> remindersList = new ArrayList<>();
        SQLiteDatabase mydb = this.getReadableDatabase();

        Cursor cursor = mydb.rawQuery("SELECT * FROM reminders WHERE u_email=?", new String[]{email}, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int gid = cursor.getInt(2);
                    String task = cursor.getString(3);
                    String details = cursor.getString(4);
                    String startDate = cursor.getString(5);
                    String endDate = cursor.getString(6);

                    GeofenceCoordinates geofenceCoordinates = getGeofence(gid);

                    Reminder reminder = new Reminder(gid, task, details, geofenceCoordinates, startDate, endDate);
                    remindersList.add(reminder);
                }
            }
        }
        return remindersList;
    }


    public Reminder getReminder(int gid) {
        Reminder reminder = null;
        SQLiteDatabase mydb = this.getReadableDatabase();

        Cursor cursor = mydb.rawQuery("SELECT * FROM reminders WHERE u_email=? AND gid=?",
                new String[]{email, String.valueOf(gid)}, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String task = cursor.getString(3);
                String details = cursor.getString(4);
                String startDate = cursor.getString(5);
                String endDate = cursor.getString(6);

                GeofenceCoordinates geofenceCoordinates = getGeofence(gid);

                reminder = new Reminder(gid, task, details, geofenceCoordinates, startDate, endDate);
            }
        }

        return reminder;
    }

    public ArrayList<Integer> getReminderGidList() {
        ArrayList<Integer> gid = new ArrayList<>();
        SQLiteDatabase mydb = this.getReadableDatabase();

        Cursor cursor = mydb.rawQuery("SELECT gid FROM reminders WHERE u_email=?",
                new String[]{email}, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                gid.add(cursor.getInt(0));
            }
        }

        return gid;
    }


    public void deleteReminder(int gid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM reminders WHERE gid=?", new String[]{String.valueOf(gid)});
    }


    // AUTOSILENT TABLE OPERATIONS

    public int addAutosilentGeofence(AutoSilent autoSilent) {
        int gid = addGeofence(autoSilent.geofenceCoordinates);

        if (gid == -1) {
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("u_email", email);
        contentValues.put("gid", gid);
        contentValues.put("title", autoSilent.getTitle());
        contentValues.put("start_date", autoSilent.getStartDate());
        contentValues.put("end_date", autoSilent.getEndDate());

        long result = db.insert("autosilent", null, contentValues);
        Log.d("autosilent", String.valueOf(result));


        if (result == -1) {
            return -1;
        }

        Global.autosilentGid = getAutosilentGidList();
        return gid;
    }

    public ArrayList<AutoSilent> getAutosilentGeofences() {
        ArrayList<AutoSilent> autoSilentGeofencesArrayList = new ArrayList<>();
        SQLiteDatabase mydb = this.getReadableDatabase();

        Cursor cursor = mydb.rawQuery("SELECT * FROM autosilent WHERE u_email=?",
                new String[]{email}, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int gid = cursor.getInt(2);
                    String title = cursor.getString(3);
                    String startDate = cursor.getString(4);
                    String endDate = cursor.getString(5);

                    GeofenceCoordinates geofenceCoordinates = getGeofence(gid);

                    AutoSilent autoSilent = new AutoSilent(title, geofenceCoordinates, startDate, endDate);
                    autoSilentGeofencesArrayList.add(autoSilent);
                }
            }
        }
        return autoSilentGeofencesArrayList;
    }


    public AutoSilent getAutosilent(int gid) {
        AutoSilent autoSilent = null;
        SQLiteDatabase mydb = this.getReadableDatabase();

        Cursor cursor = mydb.rawQuery("SELECT * FROM autosilent WHERE u_email=? AND gid=?",
                new String[]{email, String.valueOf(gid)}, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(3);
                String startDate = cursor.getString(4);
                String endDate = cursor.getString(5);

                GeofenceCoordinates geofenceCoordinates = getGeofence(gid);

                autoSilent = new AutoSilent(title, geofenceCoordinates, startDate, endDate);
            }
        }
        return autoSilent;
    }

    public ArrayList<Integer> getAutosilentGidList() {
        ArrayList<Integer> gid = new ArrayList<>();
        SQLiteDatabase mydb = this.getReadableDatabase();

        Cursor cursor = mydb.rawQuery("SELECT gid FROM autosilent WHERE u_email=?",
                new String[]{email}, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                gid.add(cursor.getInt(0));
            }
        }

        return gid;
    }
}
