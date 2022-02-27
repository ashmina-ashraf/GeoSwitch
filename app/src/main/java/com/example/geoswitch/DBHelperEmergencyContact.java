package com.example.geoswitch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperEmergencyContact extends SQLiteOpenHelper {

    public static final String DBNAME = "geoswitch.db";

    public DBHelperEmergencyContact(Context context) {
        super(context, DBNAME, null, Integer.parseInt(context.getString(R.string.db_version)));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Boolean addContact(String name, String phone) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        String email = Global.getCurrentUser().getEmail();
        ContentValues contentValues = new ContentValues();
        contentValues.put("u_email", email);
        contentValues.put("contact_name", name);
        contentValues.put("phone", phone);

        long result = MyDB.insert("emergency_contact", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Boolean getContacts() {
        EmergencyContact.clearEmergencyList();
        SQLiteDatabase mydb = this.getReadableDatabase();

        String email = Global.getCurrentUser().getEmail();
        Cursor cursor = mydb.rawQuery("SELECT phone, contact_name FROM emergency_contact WHERE u_email=?", new String[]{email}, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(1);
                    String phn = cursor.getString(0);
                    EmergencyContact newEmergencyContact = new EmergencyContact(name, phn);
                }
            }
            return true;
        }

        return false;
    }

    public void deleteContact(String phone) {
        SQLiteDatabase mydb = this.getWritableDatabase();

        String email = Global.getCurrentUser().getEmail();
        mydb.delete("emergency_contact", "u_email = ? AND phone = ?", new String[]{email, phone});
    }
}
