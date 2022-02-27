package com.example.geoswitch;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNewReminderFragment extends DialogFragment implements View.OnClickListener {

    ViewGroup root;
    EditText task, details, location, startDate, endDate, repeat;
    EditText dateSelect;
    Button add;

    DatePickerDialog.OnDateSetListener onDateSetListener;

    int year, month, day;
    String sTask, sDetails, sLocation, sStartDate, sEndDate, sRepeat;

    GeofenceHelper geofenceHelper;
    GeofencingClient geofencingClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        root = (ViewGroup) inflater.inflate(R.layout.fragment_add_new_reminder, container, false);
        task = (EditText) root.findViewById(R.id.rem_task);
        details = (EditText) root.findViewById(R.id.rem_task_details);
        location = (EditText) root.findViewById(R.id.rem_location);
        startDate = (EditText) root.findViewById(R.id.rem_start_date);
        endDate = (EditText) root.findViewById(R.id.rem_end_date);
        add = (Button) root.findViewById(R.id.rem_add);


        geofencingClient = LocationServices.getGeofencingClient(Global.getGlobalContext());
        geofenceHelper = new GeofenceHelper();

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("Set location")) {
                Log.d("reminder", bundle.getString("Set location"));
                location.setText(Global.getGeofenceCoordinates().getPlace());
            }
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Global.getGlobalContext(), AddGeofenceMap.class);
                i.putExtra("to", "reminder");
                startActivity(i);
            }
        });


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                setDate(date);
            }
        };

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInputs();

                if (validateDates(sStartDate, sEndDate)) {
                    Reminder reminder = new Reminder(sTask, sDetails, Global.getGeofenceCoordinates(),
                            sStartDate, sEndDate);

                    DBHelperGeofence dbHelperGeofence = new DBHelperGeofence(Global.getGlobalContext());
                    int gid = dbHelperGeofence.addReminder(reminder);

                    if (gid != -1) {
                        Toast.makeText(Global.getGlobalContext(), "New Reminder Added!", Toast.LENGTH_SHORT).show();
                        addGeofence(gid);
                        dismiss();
                    } else {
                        Toast.makeText(Global.getGlobalContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Global.getGlobalContext(), "Enter valid dates", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    public void getUserInputs() {
        sTask = task.getText().toString();
        sDetails = details.getText().toString();
        sStartDate = startDate.getText().toString();
        sEndDate = endDate.getText().toString();
        sLocation = location.getText().toString();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rem_start_date || v.getId() == R.id.rem_end_date) {
            dateSelect = root.findViewById(v.getId());
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener
                    , year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 1000);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        }
    }


    public void setDate(String date) {
        dateSelect.setText(date);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Task", sTask);
        outState.putString("Details", sDetails);
        outState.putString("StartDate", sStartDate);
        outState.putString("EndDate", sEndDate);
        outState.putString("Location", sLocation);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            task.setText(savedInstanceState.getString("Task"));
            details.setText(savedInstanceState.getString("Details"));
            startDate.setText(savedInstanceState.getString("StartDate"));
            endDate.setText(savedInstanceState.getString("EndDate"));
        }
    }

    private void addGeofence(int gid) {

        GeofenceCoordinates geofenceCoordinates = Global.getGeofenceCoordinates();

        List<Geofence> geofenceList = geofenceHelper.getGeofence(gid, geofenceCoordinates.latLng, geofenceCoordinates.radius,
                Geofence.GEOFENCE_TRANSITION_ENTER);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofenceList);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent("reminder");

        Global.BROADCAST_RQST = "reminder";

        if (ActivityCompat.checkSelfPermission(Global.getGlobalContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Global.getGlobalContext(), "Geofence successfully added",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = geofenceHelper.getErrorString(e);
                        Toast.makeText(Global.getGlobalContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public Boolean validateDates(String sDate, String eDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date sDate_ = simpleDateFormat.parse(sDate);
            Date eDate_ = simpleDateFormat.parse(eDate);

            if (sDate_.compareTo(eDate_) <= 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
