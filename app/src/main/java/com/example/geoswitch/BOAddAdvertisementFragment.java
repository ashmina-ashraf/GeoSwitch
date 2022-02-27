package com.example.geoswitch;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class BOAddAdvertisementFragment extends Fragment implements View.OnClickListener {

    ViewGroup root;
    EditText offer, tag, location;
    ImageView img1, img2, img3;
    Button addAd;

    ImageView selectedImg;

    String sOffer, sTag, sLocation;
    Bitmap bImg1, bImg2, bImg3;
    GeofenceCoordinates geofenceCoordinates;

    DBHelperBusinessRegister db;

    GeofencingClient geofencingClient;
    GeofenceHelper geofenceHelper;

    final int IMAGE_REQUEST_CODE = 10001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_bo_add_advertisement, container, false);


        offer = root.findViewById(R.id.bo_ad_offer);
        tag = root.findViewById(R.id.bo_ad_tagline);
        location = root.findViewById(R.id.bo_ad_location);
        img1 = root.findViewById(R.id.bo_ad_img1);
        img2 = root.findViewById(R.id.bo_ad_img2);
        img3 = root.findViewById(R.id.bo_ad_img3);
        addAd = root.findViewById(R.id.bo_ad_add);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("setLocation")) {
                location.setText(Global.getGeofenceCoordinates().getPlace());
            }
        }

        db = new DBHelperBusinessRegister(Global.getGlobalContext());

        geofencingClient = LocationServices.getGeofencingClient(Global.getGlobalContext());
        geofenceHelper = new GeofenceHelper();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Global.getGlobalContext(), AddGeofenceMap.class);
                i.putExtra("to", "bo_add_advertisement");
                startActivity(i);
            }
        });

        addAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInputs();
                Advertisement ad = new Advertisement(Global.getBusinessOwner().getBusinessName(),
                        sOffer, sTag, geofenceCoordinates, bImg1, bImg2, bImg3);

                int gid = db.addAdvertisement(ad);
                if (gid != -1) {
                    Toast.makeText(Global.getGlobalContext(), "Advertisement Posted", Toast.LENGTH_SHORT).show();
                    addGeofence(gid);
                    replaceFragment(new BOAdvertisementFragment());
                } else {
                    Toast.makeText(Global.getGlobalContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);

        return root;
    }

    private void getUserInputs() {
        sOffer = offer.getText().toString();
        sTag = tag.getText().toString();
        sLocation = location.getText().toString();

        BitmapDrawable bitmapDrawable1 = (BitmapDrawable) img1.getDrawable();
        bImg1 = bitmapDrawable1.getBitmap();
        BitmapDrawable bitmapDrawable2 = (BitmapDrawable) img2.getDrawable();
        bImg2 = bitmapDrawable2.getBitmap();
        BitmapDrawable bitmapDrawable3 = (BitmapDrawable) img3.getDrawable();
        bImg3 = bitmapDrawable3.getBitmap();

        geofenceCoordinates = Global.getGeofenceCoordinates();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        selectedImg = root.findViewById(v.getId());

        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Global.getGlobalContext().getContentResolver(), uri);
                    selectedImg.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void addGeofence(int gid) {
        DBHelperGeofence dbGeofence = new DBHelperGeofence(Global.getGlobalContext());
        GeofenceCoordinates geofenceCoordinates = dbGeofence.getGeofence(gid);

        List<Geofence> geofenceList = geofenceHelper.getGeofence(gid, geofenceCoordinates.latLng, geofenceCoordinates.radius,
                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofenceList);
        PendingIntent pendingIntent = geofenceHelper.getAdPendingIntent("boAd");

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


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bo_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}