package com.example.geoswitch;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.geoswitch.databinding.ActivityHomeBinding;

import maes.tech.intentanim.CustomIntent;

public class HomeActivity extends AppCompatActivity {

    SessionManagement sessionManagement;
    DBHelper db;
    User user;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    ActivityHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        if (getIntent().hasExtra("to")) {
            Bundle bundle = new Bundle();
            Intent i;
            switch (getIntent().getStringExtra("to")) {
                case "reminder":
                    ToDoFragment toDoFragment = new ToDoFragment();
                    bundle.putString("to", "reminder");
                    toDoFragment.setArguments(bundle);
                    replaceFragment(toDoFragment);
                    break;

                case "autosilent":
                    AutoSilentFragment autoSilentFragment = new AutoSilentFragment();
                    bundle.putString("to", "autosilent");
                    autoSilentFragment.setArguments(bundle);
                    replaceFragment(autoSilentFragment);
                    break;
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.navigation_reminder:
                    replaceFragment(new ReminderFragment());
                    break;

                case R.id.navigation_settings:
                    replaceFragment(new SettingsFragment());
                    break;

                case R.id.navigation_emergency:
                    replaceFragment(new EmergencyFragment());
                    break;
            }

            return true;
        });

        sessionManagement = new SessionManagement(this);
        db = new DBHelper(this);

        String email = sessionManagement.getSession()[1];
        user = db.getUserInfo(email);


        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }

    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }


    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction("startLocationService");
            startService(intent);

            Toast.makeText(this, "Location Service Started", Toast.LENGTH_SHORT).show();
        }
    }
}