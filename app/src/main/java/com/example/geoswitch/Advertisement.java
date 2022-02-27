package com.example.geoswitch;

import android.graphics.Bitmap;

public class Advertisement {

    String offer, tagline, businessName;
    GeofenceCoordinates geofenceCoordinates;
    Bitmap img1 = null, img2 = null, img3 = null;


    public Advertisement(String businessName, String offer, String tagline,
                         GeofenceCoordinates geofenceCoordinates, Bitmap img1, Bitmap img2, Bitmap img3) {
        this.businessName = businessName;
        this.offer = offer;
        this.tagline = tagline;
        this.geofenceCoordinates = geofenceCoordinates;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
    }

    public Advertisement(String offer, String tagline, GeofenceCoordinates geofenceCoordinates, Bitmap img1) {
        this.offer = offer;
        this.tagline = tagline;
        this.geofenceCoordinates = geofenceCoordinates;
        this.img1 = img1;
    }

    public Advertisement(String offer, String tagline, GeofenceCoordinates geofenceCoordinates, Bitmap img1, Bitmap img2) {
        this.offer = offer;
        this.tagline = tagline;
        this.geofenceCoordinates = geofenceCoordinates;
        this.img1 = img1;
        this.img2 = img2;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public GeofenceCoordinates getGeofenceCoordinates() {
        return geofenceCoordinates;
    }

    public void setGeofenceCoordinates(GeofenceCoordinates geofenceCoordinates) {
        this.geofenceCoordinates = geofenceCoordinates;
    }

    public Bitmap getImg1() {
        return img1;
    }

    public void setImg1(Bitmap img1) {
        this.img1 = img1;
    }

    public Bitmap getImg2() {
        return img2;
    }

    public void setImg2(Bitmap img2) {
        this.img2 = img2;
    }

    public Bitmap getImg3() {
        return img3;
    }

    public void setImg3(Bitmap img3) {
        this.img3 = img3;
    }
}
