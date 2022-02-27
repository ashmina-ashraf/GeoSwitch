package com.example.geoswitch;

public class AutoSilent {

    String title, startDate, endDate;
    GeofenceCoordinates geofenceCoordinates;

    public AutoSilent(String sTitle, GeofenceCoordinates geofenceCoordinates, String sStartDate, String sEndDate) {
        this.title = sTitle;
        this.startDate = sStartDate;
        this.endDate = sEndDate;
        this.geofenceCoordinates = geofenceCoordinates;
    }

    public AutoSilent(AutoSilent autoSilent) {
        this.title = autoSilent.getTitle();
        this.startDate = autoSilent.getStartDate();
        this.endDate = autoSilent.getEndDate();
        this.geofenceCoordinates = autoSilent.getGeofenceCoordinates();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public GeofenceCoordinates getGeofenceCoordinates() {
        return geofenceCoordinates;
    }

    public void setGeofenceCoordinates(GeofenceCoordinates geofenceCoordinates) {
        this.geofenceCoordinates = geofenceCoordinates;
    }
}
