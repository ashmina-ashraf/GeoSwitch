package com.example.geoswitch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "geoswitch.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, Integer.parseInt(context.getString(R.string.db_version)));
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Users Table
        db.execSQL("CREATE TABLE IF NOT EXISTS users(uid integer primary key autoincrement," +
                "username varchar(25), password varchar(25), email varchar(50) )");

        //Emergency contacts table
        db.execSQL("CREATE TABLE IF NOT EXISTS emergency_contact(eid integer primary key autoincrement," +
                "u_email varchar(50), phone varchar(10), contact_name varchar(25))");

        //Geofences Table
        db.execSQL("CREATE TABLE IF NOT EXISTS geofences(gid integer primary key autoincrement," +
                "u_email varchar(50), latitude real, longitude real, radius real )");

        //Reminders table
        db.execSQL("CREATE TABLE IF NOT EXISTS reminders(rid integer primary key autoincrement," +
                "u_email varchar(50), gid integer, task varchar(100), details varchar(200), " +
                "start_date varchar(25), end_date varchar(25))");

        //AutoSilent table
        db.execSQL("CREATE TABLE IF NOT EXISTS autosilent(aid integer primary key autoincrement," +
                "u_email varchar(50), gid integer, title varchar(100)," +
                "start_date varchar(25), end_date varchar(25))");

        //Business Owner table
        db.execSQL("CREATE TABLE IF NOT EXISTS business_owners(bid integer primary key autoincrement, email varchar(50), " +
                "business_name varchar(25), password varchar(25), business_type varchar(25), " +
                "latitude real, longitude real, subscription integer)");

        //Advertisement table
        db.execSQL("CREATE TABLE IF NOT EXISTS advertisements(ad_id integer primary key autoincrement, email varchar(50), " +
                "gid integer, business_name varchar(25), offer text, tagline text, " +
                "img1 blob, img2 blob, img3 blob)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS emergency_contact");
        db.execSQL("DROP TABLE IF EXISTS geofences");
        db.execSQL("DROP TABLE IF EXISTS reminders");
        db.execSQL("DROP TABLE IF EXISTS autosilent");
        db.execSQL("DROP TABLE IF EXISTS business_owners");
        db.execSQL("DROP TABLE IF EXISTS advertisements");
    }


    public Boolean insertData(String username, String password, String email) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);

        long result = MyDB.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Boolean checkUserExists(String email) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public int checkEmailPassword(String email, String pswd) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Boolean ifUserExists = checkUserExists(email);

        if (ifUserExists) {
            Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?",
                    new String[]{email, pswd});

            if (cursor.getCount() > 0) {
                return 0;               // Login successful
            } else {
                return 1;               // Incorrect password
            }
        } else {
            return 2;                  // No user exist
        }
    }


    public User getUserInfo(String email) {
        SQLiteDatabase mydb = this.getReadableDatabase();

        Cursor cursor = mydb.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        while (cursor.moveToNext()) {
            String mail = cursor.getString(3);
            String uname = cursor.getString(1);
            return new User(mail, uname);
        }

        return null;
    }


}
