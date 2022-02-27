package com.example.geoswitch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

public class ReminderFragment extends Fragment implements View.OnClickListener {

    GeofencingClient geofencingClient;
    LinearLayout reminder, autoSilent, callFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_reminder, container, false);

        reminder = root.findViewById(R.id.reminder);
        autoSilent = root.findViewById(R.id.auto_silent);
        callFilter = root.findViewById(R.id.call_filter);

        reminder.setOnClickListener(this);
        autoSilent.setOnClickListener(this);
        callFilter.setOnClickListener(this);

        geofencingClient = LocationServices.getGeofencingClient(Global.getGlobalContext());
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reminder:
                replaceFragment(new ToDoFragment());
                break;
            case R.id.auto_silent:
                replaceFragment(new AutoSilentFragment());
                break;
            case R.id.call_filter:
                replaceFragment(new ReminderFragment());
                break;

        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}