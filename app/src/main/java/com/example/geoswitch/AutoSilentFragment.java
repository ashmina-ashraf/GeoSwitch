package com.example.geoswitch;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class AutoSilentFragment extends Fragment {

    LinearLayout addPlaceBtn;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_auto_silent, container, false);

        addPlaceBtn = (LinearLayout) root.findViewById(R.id.add_autosilent_geofence);
        recyclerView = (RecyclerView) root.findViewById(R.id.autosilent_recycler_view);
        updateUI();

        addPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewAutoSilentGeofence addNewAutoSilentGeofence = new AddNewAutoSilentGeofence();
                addNewAutoSilentGeofence.show(getParentFragmentManager(), "Reminder");
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("to")) {
                AddNewAutoSilentGeofence addNewAutoSilentGeofence = new AddNewAutoSilentGeofence();
                Bundle bundle1 = new Bundle();
                bundle1.putString("Set location", "1");
                addNewAutoSilentGeofence.setArguments(bundle1);
                addNewAutoSilentGeofence.show(getParentFragmentManager(), "Autosilent");
            }
        }


        //Getting DO NOT DISTURB MODE PERMISSION
        NotificationManager notificationManager =
                (NotificationManager) Global.getGlobalContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent i = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            getActivity().startActivity(i);
        }

        return root;
    }

    private void recycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false));

        DBHelperGeofence db = new DBHelperGeofence(Global.getGlobalContext());
        ArrayList<AutoSilent> autoSilentArrayList = db.getAutosilentGeofences();

        AutoSilentAdapter adapter = new AutoSilentAdapter(autoSilentArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void updateUI() {
        recycler();
    }

}