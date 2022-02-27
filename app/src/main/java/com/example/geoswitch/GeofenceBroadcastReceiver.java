package com.example.geoswitch;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    Context context;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    DBHelperGeofence db;
    GeofencingEvent geofencingEvent;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        geofencingEvent = GeofencingEvent.fromIntent(intent);
        db = new DBHelperGeofence(context);

        List<Geofence> triggerList = geofencingEvent.getTriggeringGeofences();

        int i = 0;
        int[] gid = new int[triggerList.size()];

        while (i < triggerList.size()) {
            gid[i] = Integer.parseInt(triggerList.get(i).getRequestId());
            if (Global.reminderGid.contains(gid[i])) {
                reminder(gid[i]);
            } else if (Global.autosilentGid.contains(gid[i])) {
                autosilent(gid[i]);
            }
            i++;
        }
    }

    public void reminder(int gid) {
        if (db.getReminder(gid) != null) {
            Reminder reminder = db.getReminder(gid);
            sendReminderNotification(reminder.getTask(), reminder.getDetails());
        }
    }

    public void autosilent(int gid) {
        int transition = geofencingEvent.getGeofenceTransition();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (db.getAutosilent(gid) != null) {
            String title = db.getAutosilent(gid).getTitle();

            switch (transition) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    sendAutosilentNotification(true, title);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    sendAutosilentNotification(false, title);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
            }
        }
    }

    public void constructNotification(String title, String details) {
        String channelId = "Geoswitch";
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.icon_notification_image)
                .setContentTitle(title)
                .setContentText(details)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVibrate(new long[]{0, 100, 200, 300})
                .setSound(alarmSound);

        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null
                    && notificationManager.getNotificationChannel(channelId) == null) {

                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Reminder",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by reminder");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    public void sendAutosilentNotification(Boolean enter, String place) {
        String title, details;
        if (enter) {
            title = "Turning silent mode on";
            details = "You entered " + place;
        } else {
            title = "Turning silent mode off";
            details = "You exited " + place;
        }
        constructNotification(title, details);
        notificationManager.notify(1, builder.build());
    }

    public void sendReminderNotification(String task, String details) {
        constructNotification(task, details);

        Intent intentDone = new Intent(context, GeofenceBroadcastReceiver.class);
        intentDone.putExtra("done", true);
        intentDone.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent1 =
                PendingIntent.getActivity(context, 0, intentDone, PendingIntent.FLAG_ONE_SHOT);

        Intent intentRepeat = new Intent(context, GeofenceBroadcastReceiver.class);
        intentDone.putExtra("repeat", true);
        intentDone.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntentRepeat =
                PendingIntent.getActivity(context, 0, intentRepeat, PendingIntent.FLAG_ONE_SHOT);


        builder.addAction(R.drawable.icon_home, "Done", pendingIntent1);
        builder.addAction(R.drawable.icon_settings, "Repeat", pendingIntentRepeat);

        notificationManager.notify(1, builder.build());
    }
}