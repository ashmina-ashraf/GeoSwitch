package com.example.geoswitch;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GeofenceHelper {
    private static List<Geofence> geofenceList = new ArrayList<Geofence>();
    private PendingIntent pendingIntent;
    private PendingIntent adPendingIntent;
    private final int BROADCAST_REQUEST_CODE = 2607;

    public GeofencingRequest getGeofencingRequest(List<Geofence> geofences) {
        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .addGeofences(geofences)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();

        return geofencingRequest;
    }

    public List<Geofence> getGeofence(int gid, LatLng latlng, float radius, int transitionType) {
        geofenceList.add(new Geofence.Builder()
                .setRequestId(String.valueOf(gid))
                .setCircularRegion(latlng.latitude, latlng.longitude, radius)
                .setTransitionTypes(transitionType)
                .setLoiteringDelay(5000)  //For dwell transition type
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build());

        Log.d("testing", geofenceList.toString());
        return geofenceList;
    }

    public PendingIntent getAdPendingIntent(String from) {
        if (adPendingIntent != null) {
            return adPendingIntent;
        }

        Intent intent = new Intent(Global.getGlobalContext(), AdvertisementGeofenceReceiver.class);
        intent.putExtra(from, "val");
        adPendingIntent = PendingIntent.getBroadcast(Global.getGlobalContext(),
                BROADCAST_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("testing", adPendingIntent.toString());
        return adPendingIntent;
    }


    public PendingIntent getPendingIntent(String from) {
        if (pendingIntent != null) {
            return pendingIntent;
        }

        Intent intent = new Intent(Global.getGlobalContext(), GeofenceBroadcastReceiver.class);
        intent.setAction("geofence");
        intent.putExtra(from, "val");
        pendingIntent = PendingIntent.getBroadcast(Global.getGlobalContext(),
                BROADCAST_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("testing", pendingIntent.toString());
        return pendingIntent;
    }

    public String getErrorString(Exception e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;

            switch (apiException.getStatusCode()) {
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "Not available";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "Too many geofences";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "Too many pending intents";
            }
        }
        return e.getLocalizedMessage();
    }

}
