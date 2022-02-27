package com.example.geoswitch;

import java.util.ArrayList;

public class Reminder {

    int gid;
    String task, details, startDate, endDate;
    GeofenceCoordinates geofenceCoordinates;

    public Reminder(String task, String details, GeofenceCoordinates geofenceCoordinates, String startDate, String endDate) {
        this.task = task;
        this.details = details;
        this.startDate = startDate;
        this.endDate = endDate;
        this.geofenceCoordinates = geofenceCoordinates;
    }

    public Reminder(Reminder reminder) {
        this.gid = reminder.getGid();
        this.task = reminder.getTask();
        this.details = reminder.getDetails();
        this.startDate = reminder.getStartDate();
        this.endDate = reminder.getEndDate();
        this.geofenceCoordinates = reminder.getGeofenceCoordinates();
    }

    public Reminder(int gid, String task, String details,
                    GeofenceCoordinates geofenceCoordinates, String startDate, String endDate) {
        this.gid = gid;
        this.task = task;
        this.details = details;
        this.startDate = startDate;
        this.endDate = endDate;
        this.geofenceCoordinates = geofenceCoordinates;
    }

    public GeofenceCoordinates getGeofenceCoordinates() {
        return geofenceCoordinates;
    }

    public void setGeofenceCoordinates(GeofenceCoordinates geofenceCoordinates) {
        this.geofenceCoordinates = geofenceCoordinates;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }
}
