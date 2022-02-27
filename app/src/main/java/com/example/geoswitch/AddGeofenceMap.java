package com.example.geoswitch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.geoswitch.databinding.ActivityAddGeofenceMapBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddGeofenceMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityAddGeofenceMapBinding binding;
    private Marker currentMarker;
    private CircleOptions circleOptions;

    private float geofenceRadius = 100;

    ImageView add;
    SeekBar radiusSeekBar;
    TextView radiusTv;
    LatLng selectedLatLng = new LatLng(0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddGeofenceMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        add = findViewById(R.id.add_geofence);
        radiusSeekBar = findViewById(R.id.radius_seekbar);
        radiusTv = findViewById(R.id.radius_tv);

        if (getIntent().getExtras().getString("to").equals("bo_register")) {
            radiusSeekBar.setVisibility(View.GONE);
            radiusTv.setVisibility(View.GONE);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGeofence();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();

        mMap.setOnMapLongClickListener(this);
    }


    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);

        showCurrentLocation();
    }


    private void showCurrentLocation() {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(CurrentLocation.latitude, CurrentLocation.longitude))
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker());

        CameraUpdate cameraUpdate =
                CameraUpdateFactory.newLatLngZoom(new LatLng(CurrentLocation.latitude, CurrentLocation.longitude), 17);


        if (currentMarker != null) {
            currentMarker.remove();
        }

        currentMarker = mMap.addMarker(markerOptions);
        mMap.animateCamera(cameraUpdate);
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        selectedLatLng = latLng;
        addMarker(latLng);

        if (!getIntent().getExtras().getString("to").equals("bo_register")) {
            addCircle(latLng, geofenceRadius);

            radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    radiusTv.setText(String.valueOf(progress) + "m");
                    geofenceRadius = progress;
                    mMap.clear();
                    addCircle(latLng, geofenceRadius);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    private void selectGeofence() {
        Intent i;

        if (selectedLatLng == new LatLng(0, 0)) {
            Toast.makeText(this, "Select a location", Toast.LENGTH_SHORT).show();
        } else if (getIntent().getExtras().getString("to").equals("bo_register")) {
            i = new Intent(this, BusinessRegisterActivity.class);
            Location location = new Location(selectedLatLng);
            i.putExtra("Location", true);
            i.putExtra("Latitude", selectedLatLng.latitude);
            i.putExtra("Longitude", selectedLatLng.longitude);
            startActivity(i);
        } else {
            GeofenceCoordinates geofenceCoordinates = new GeofenceCoordinates(selectedLatLng, geofenceRadius);
            Global.setGeofenceCoordinate(geofenceCoordinates);

            if (getIntent().hasExtra("to")) {
                switch (getIntent().getExtras().getString("to")) {
                    case "reminder":
                        i = new Intent(Global.getGlobalContext(), HomeActivity.class);
                        i.putExtra("to", "reminder");
                        startActivity(i);
                        break;

                    case "autosilent":
                        i = new Intent(Global.getGlobalContext(), HomeActivity.class);
                        i.putExtra("to", "autosilent");
                        startActivity(i);
                        break;

                    case "bo_add_advertisement":
                        i = new Intent(Global.getGlobalContext(), BOHomeActivity.class);
                        i.putExtra("to", "bo_add_advertisement");
                        startActivity(i);
                        break;
                }
            }
        }
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions newMarker = new MarkerOptions().position(latLng);
        mMap.addMarker(newMarker);
    }

    private void addCircle(LatLng latLng, float radius) {
        circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(64, 255, 0, 0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }
}