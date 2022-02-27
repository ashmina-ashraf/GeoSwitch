package com.example.geoswitch;

import com.google.android.gms.maps.model.LatLng;

public class GeofenceCoordinates extends Location {
    int gid;
    Float radius;

    public GeofenceCoordinates(LatLng latLng, float radius) {
        super(latLng);
        this.radius = radius;
    }

    public GeofenceCoordinates(Double latitude, Double longitude, float radius) {
        super(latitude, longitude);
        this.radius = radius;
    }

    public GeofenceCoordinates(String place, float radius) {
        super(place);
        this.radius = radius;
    }

    public GeofenceCoordinates(int gid, double latitude, double longitude, float radius) {
        super(latitude, longitude);
        this.radius = radius;
        this.gid = gid;
    }

    public Float getRadius() {
        return radius;
    }

    public int getGid() {
        return gid;
    }
}
