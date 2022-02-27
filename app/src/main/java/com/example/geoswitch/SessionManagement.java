package com.example.geoswitch;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static String pref_name = "Session";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserType(int userType, String email) {
        editor.putInt("userType", userType);
        saveSession(email);
        Global.setUserType(userType);

        if (userType == Global.USER) {
            DBHelper db = new DBHelper(Global.getGlobalContext());
            Global.setCurrentUser(db.getUserInfo(email));
        } else if (userType == Global.BUSINESS_OWNER) {
            DBHelperBusinessRegister db = new DBHelperBusinessRegister(Global.getGlobalContext());
            Global.setCurrentUser(db.getBusinessOwnerInfo(email));
        }
    }

    public void saveSession(String email) {
        editor.putString("user", email);
        editor.commit();
    }

    public String[] getSession() {
        return new String[]{String.valueOf(sharedPreferences.getInt("userType", -1)),
                sharedPreferences.getString("user", null)};
    }

    public void removeSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userType", -1);
        editor.putString("user", null);
        editor.commit();
    }
}
