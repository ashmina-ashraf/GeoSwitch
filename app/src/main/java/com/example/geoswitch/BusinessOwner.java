package com.example.geoswitch;

public class BusinessOwner {
    public static String email;
    public static String businessName;
    public static String businessType;
    public static int subscription;
    public static Location location;


    public BusinessOwner(String email, String businessName,
                         String businessType, int subscription, Location location) {
        BusinessOwner.email = email;
        BusinessOwner.businessName = businessName;
        BusinessOwner.businessType = businessType;
        BusinessOwner.subscription = subscription;
        BusinessOwner.location = location;
    }

    public static String getEmail() {
        return email;
    }

    public static String getBusinessName() {
        return businessName;
    }

    public static void setGeofenceCoordinates(Location location) {
        BusinessOwner.location = location;
    }

    public static String getBusinessType() {
        return businessType;
    }

    public static int getSubscription() {
        return subscription;
    }

    public static void setSubscription(int subscription) {
        BusinessOwner.subscription = subscription;
    }

    public static Location getGeofenceCoordinates() {
        return location;
    }
}
