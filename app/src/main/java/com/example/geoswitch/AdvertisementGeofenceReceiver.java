package com.example.geoswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class AdvertisementGeofenceReceiver extends BroadcastReceiver {

    GeofencingEvent geofencingEvent;

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Toast.makeText(context, "Triggered", Toast.LENGTH_SHORT).show();

        DBHelperBusinessRegister db = new DBHelperBusinessRegister(context);
        geofencingEvent = GeofencingEvent.fromIntent(intent);

        List<Geofence> triggerList = geofencingEvent.getTriggeringGeofences();

        int i = 0;
        int[] gid = new int[triggerList.size()];

        while (i < triggerList.size()) {
            gid[i] = Integer.parseInt(triggerList.get(i).getRequestId());
            i++;
        }

        int transition = geofencingEvent.getGeofenceTransition();
        Log.d("testing", String.valueOf(transition));

        switch (transition) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                for (int j = 0; j < gid.length; j++) {
                    Global.adList.add(gid[j]);
                    Log.d("testing", "enter");
                }
                break;

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                for (int j = 0; j < gid.length; j++) {
                    Global.adList.remove(new Integer(gid[j]));
                    Log.d("testing", "exit");
                }
                break;
        }


    }
}