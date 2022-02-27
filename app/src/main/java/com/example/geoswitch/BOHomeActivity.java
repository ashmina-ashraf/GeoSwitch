package com.example.geoswitch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.geoswitch.databinding.ActivityBoHomeBinding;
import com.example.geoswitch.databinding.ActivityHomeBinding;

public class BOHomeActivity extends AppCompatActivity {

    SessionManagement sessionManagement;
    DBHelper db;
    BusinessOwner businessOwner;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    ActivityBoHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new BOAdvertisementFragment());

        if (getIntent().hasExtra("to")) {
            Bundle bundle = new Bundle();
            Intent i;
            switch (getIntent().getStringExtra("to")) {
                case "bo_add_advertisement":
                    BOAddAdvertisementFragment boAddAdvertisementFragment = new BOAddAdvertisementFragment();
                    bundle.putString("setLocation", "true");
                    boAddAdvertisementFragment.setArguments(bundle);
                    replaceFragment(boAddAdvertisementFragment);
                    break;
            }
        }

        binding.boBottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bo_home:
                    replaceFragment(new BOAdvertisementFragment());
                    break;

                case R.id.bo_subscription:
                    replaceFragment(new BOSubscriptionFragment());
                    break;

                case R.id.bo_settings:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;
        });


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
        fragmentTransaction.replace(R.id.bo_frame_layout, fragment);
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