package com.example.geoswitch;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import maes.tech.intentanim.CustomIntent;

public class SettingsFragment extends Fragment {

    Button logoutBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        logoutBtn = (Button) root.findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return root;
    }

    private void logout() {
        SessionManagement sessionManagement = new SessionManagement(getContext());
        sessionManagement.removeSession();
        stopLocationService();
        goToLogin();
    }

    public void stopLocationService() {
        Intent intent = new Intent(getContext(), LocationService.class);
        intent.setAction("stopLocationService");
        getContext().startService(intent);

        Toast.makeText(getContext(), "Location Service Stopped", Toast.LENGTH_SHORT).show();
    }


    private void goToLogin() {
        Intent i = new Intent(getContext(), MainActivity.class);
        i.putExtra("tab", "0");
        startActivity(i);
        CustomIntent.customType(getContext(), "left-to-right");
    }
}