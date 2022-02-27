package com.example.geoswitch;

import android.app.Application;

import java.util.ArrayList;

public class Global extends Application {

    private static Global instance;
    private static User user;
    private static BusinessOwner businessOwner;
    private static GeofenceCoordinates geofenceCoordinates;

    SessionManagement sessionManagement;
    DBHelper db;
    DBHelperBusinessRegister mydb;

    public static String BROADCAST_RQST = "reminder";

    public static final int USER = 1;
    public static final int BUSINESS_OWNER = 2;
    public static final int ADMIN = 0;
    private static int userType = ADMIN;

    public static ArrayList<Integer> reminderGid;
    public static ArrayList<Integer> autosilentGid;
    public static ArrayList<Integer> adList = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        sessionManagement = new SessionManagement(this);
        String[] session = sessionManagement.getSession();
        userType = Integer.parseInt(session[0]);
        String email = session[1];

        if (email != null) {
            if (userType == Global.USER) {
                db = new DBHelper(this);
                user = db.getUserInfo(email);

                DBHelperGeofence dbHelperGeofence = new DBHelperGeofence(this);
                reminderGid = dbHelperGeofence.getReminderGidList();
                autosilentGid = dbHelperGeofence.getAutosilentGidList();
            } else {
                mydb = new DBHelperBusinessRegister(this);
                businessOwner = mydb.getBusinessOwnerInfo(email);
            }
        }
    }

    public static void setCurrentUser(User user) {
        Global.user = user;
    }

    public static Global getGlobalContext() {
        return instance;
    }

    public static void setCurrentUser(BusinessOwner businessOwner) {
        Global.businessOwner = businessOwner;
    }

    public static User getCurrentUser() {
        return user;
    }

    public static BusinessOwner getBusinessOwner() {
        return businessOwner;
    }

    public static void setGeofenceCoordinate(GeofenceCoordinates geofenceCoordinates) {
        Global.geofenceCoordinates = geofenceCoordinates;
    }

    public static GeofenceCoordinates getGeofenceCoordinates() {
        return geofenceCoordinates;
    }

    public static void clearGeofenceCoordinate() {
        Global.setGeofenceCoordinate(null);
    }

    public static int getUserType() {
        return userType;
    }

    public static void setUserType(int userType) {
        Global.userType = userType;
    }
}
