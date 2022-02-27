package com.example.geoswitch;

import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public class Location {

    LatLng latLng;
    Double latitude;
    Double longitude;
    String place;

    Geocoder geocoder;

    public Location(LatLng latLng) {
        this.latLng = latLng;
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;

        try {
            geocoder = new Geocoder(Global.getGlobalContext());
            this.place = String.valueOf(geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.latLng = new LatLng(latitude, longitude);

        try {
            geocoder = new Geocoder(Global.getGlobalContext());
            this.place = String.valueOf(geocoder.getFromLocation(latitude, longitude, 1)
                    .get(0).getAddressLine(0));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location(String place) {
        this.place = place;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public String getPlace() {
        return place;
    }
}
