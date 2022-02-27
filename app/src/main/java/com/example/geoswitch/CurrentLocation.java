package com.example.geoswitch;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

public class CurrentLocation {

    public static double latitude = 0.0;
    public static double longitude = 0.0;

    public CurrentLocation(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    public double getCurrentLatitude() {
        return latitude;
    }

    public double getCurrentLongitude() {
        return longitude;
    }


    public static String getCurrentLocation(Context context) {
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> location = geocoder.getFromLocation(CurrentLocation.latitude, CurrentLocation.longitude, 1);
            if (location.size() != 0) {
                return String.valueOf(location.get(0).getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
