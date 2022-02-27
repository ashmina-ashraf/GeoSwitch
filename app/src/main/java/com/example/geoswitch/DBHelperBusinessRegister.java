package com.example.geoswitch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DBHelperBusinessRegister extends SQLiteOpenHelper {

    public static final String DBNAME = "geoswitch.db";
    Context context;

    public DBHelperBusinessRegister(Context context) {
        super(context, DBNAME, null, Integer.parseInt(context.getString(R.string.db_version)));
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        DBHelper dbHelper = new DBHelper(context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public Boolean insertData(String email, String bus_name, String password, String bus_type, Location location) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("business_name", bus_name);
        contentValues.put("password", password);
        contentValues.put("business_type", bus_type);
        contentValues.put("latitude", location.getLatitude());
        contentValues.put("longitude", location.getLongitude());
        contentValues.put("subscription", 0);

        long result = db.insert("business_owners", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkBusinessOwnerExists(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM business_owners WHERE email = ?", new String[]{email}, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int checkEmailPassword(String email, String pswd) {
        SQLiteDatabase db = this.getReadableDatabase();

        Boolean ifBOExists = checkBusinessOwnerExists(email);

        if (ifBOExists) {
            Cursor cursor = db.rawQuery("SELECT * FROM business_owners WHERE email = ? AND password = ?",
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

    public BusinessOwner getBusinessOwnerInfo(String email) {
        BusinessOwner bo = null;
        SQLiteDatabase mydb = this.getReadableDatabase();

        Cursor cursor = mydb.rawQuery("SELECT * FROM business_owners WHERE email = ?", new String[]{email});

        while (cursor.moveToNext()) {
            String mail = cursor.getString(1);
            String bname = cursor.getString(2);
            String btype = cursor.getString(4);
            Double lat = cursor.getDouble(5);
            Double lng = cursor.getDouble(6);
            int subscription = cursor.getInt(7);

            return bo = new BusinessOwner(mail, bname, btype, subscription, new Location(lat, lng));
        }

        return bo;
    }


    //Advertisement Table Operations

    public int addAdvertisement(Advertisement ad) {
        String email = Global.getBusinessOwner().getEmail();
        SQLiteDatabase db = this.getWritableDatabase();

        DBHelperGeofence dbGeofence = new DBHelperGeofence(context);
        int gid = dbGeofence.addGeofence(ad.geofenceCoordinates);

        if (gid == -1) {
            return -1;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put("gid", gid);
        contentValues.put("email", email);
        contentValues.put("business_name", ad.getBusinessName());
        contentValues.put("offer", ad.getOffer());
        contentValues.put("tagline", ad.getTagline());
        contentValues.put("img1", convertImageToByte(ad.getImg1()));
        contentValues.put("img2", convertImageToByte(ad.getImg2()));
        contentValues.put("img3", convertImageToByte(ad.getImg3()));

        long result = db.insert("advertisements", null, contentValues);
        if (result == -1) {
            return -1;
        }

        return gid;
    }

    private byte[] convertImageToByte(Bitmap img) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    public Advertisement getAdvertisement(int gid) {
        Advertisement ad = null;
        SQLiteDatabase mydb = this.getReadableDatabase();

        Cursor cursor = mydb.rawQuery("SELECT * FROM advertisements WHERE gid=?", new String[]{String.valueOf(gid)});

        while (cursor.moveToNext()) {
            String email = cursor.getString(1);
            String businessName = cursor.getString(3);
            String offer = cursor.getString(4);
            String tagline = cursor.getString(5);

            Bitmap img1 = convertByteToBitmap(cursor.getBlob(6));
            Bitmap img2 = convertByteToBitmap(cursor.getBlob(7));
            Bitmap img3 = convertByteToBitmap(cursor.getBlob(8));

            DBHelperGeofence dbGeofence = new DBHelperGeofence(context);
            GeofenceCoordinates geofenceCoordinates = dbGeofence.getGeofence(email, gid);
            Log.d("testing", String.valueOf(geofenceCoordinates));

            ad = new Advertisement(businessName, offer, tagline, geofenceCoordinates, img1, img2, img3);
        }

        return ad;
    }


    public ArrayList<Advertisement> getBOAdvertisements() {
        ArrayList<Advertisement> adList = new ArrayList<>();
        String email = Global.getBusinessOwner().getEmail();

        SQLiteDatabase mydb = this.getReadableDatabase();

        Cursor cursor = mydb.rawQuery("SELECT * FROM advertisements WHERE email =?", new String[]{email});

        while (cursor.moveToNext()) {
            int gid = cursor.getInt(2);
            String businessName = cursor.getString(3);
            String offer = cursor.getString(4);
            String tagline = cursor.getString(5);

            Bitmap img1 = convertByteToBitmap(cursor.getBlob(6));
            Bitmap img2 = convertByteToBitmap(cursor.getBlob(7));
            Bitmap img3 = convertByteToBitmap(cursor.getBlob(8));

            DBHelperGeofence dbGeofence = new DBHelperGeofence(context);
            GeofenceCoordinates geofenceCoordinates = dbGeofence.getGeofence(gid);

            Advertisement ad = new Advertisement(businessName, offer, tagline, geofenceCoordinates, img1, img2, img3);
            adList.add(ad);
        }

        return adList;
    }

    private Bitmap convertByteToBitmap(byte[] img) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        return bitmap;
    }

}
