package com.example.geoswitch;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoswitch.HelperClasses.HomeAdapter.PlaceCategoryAdapter;
import com.example.geoswitch.HelperClasses.HomeAdapter.PlaceCategoryRecyclerHelperClass;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    TextView userLocation;
    RecyclerView recyclerView, adRecycler;
    PlaceCategoryAdapter placeCategoryAdapter;

    public static ArrayList<PlaceCategoryRecyclerHelperClass> placeCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        userLocation = (TextView) root.findViewById(R.id.user_location);
        recyclerView = (RecyclerView) root.findViewById(R.id.place_cat_recycler_view);
        adRecycler = (RecyclerView) root.findViewById(R.id.user_ad_view);
        adRecycler.setHasFixedSize(true);
        adRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        updateLoc();
        placeCatRecycler();
        advertisementRecycler();

        return root;
    }

    private void placeCatRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        placeCategory = new ArrayList<>();
        placeCategory.add(new PlaceCategoryRecyclerHelperClass(R.drawable.icon_restaurant, "Restaurant", "Restaurant"));
        placeCategory.add(new PlaceCategoryRecyclerHelperClass(R.drawable.icon_hospital, "Hospital  ", "Hospital"));
        placeCategory.add(new PlaceCategoryRecyclerHelperClass(R.drawable.icon_gas_station, "Gas station", "Gas station"));
        placeCategory.add(new PlaceCategoryRecyclerHelperClass(R.drawable.icon_hotel, "Hotel", "Hotel"));

        placeCategoryAdapter = new PlaceCategoryAdapter(placeCategory);
        recyclerView.setAdapter(placeCategoryAdapter);
    }


    private void advertisementRecycler() {
        DBHelperBusinessRegister dbHelperBusinessRegister = new DBHelperBusinessRegister(Global.getGlobalContext());
        ArrayList<Advertisement> adList = new ArrayList<>();

        for (int i = 0; i < Global.adList.size(); i++) {
            adList.add(dbHelperBusinessRegister.getAdvertisement(Global.adList.get(i)));
        }

        UserAdvertisementAdapter adapter = new UserAdvertisementAdapter(adList);
        adRecycler.setAdapter(adapter);
    }


    public void updateLoc() {
        String currentLoc = CurrentLocation.getCurrentLocation(Global.getGlobalContext());
        advertisementRecycler();
        if (currentLoc != null) {
            userLocation.setText(currentLoc);
        } else {
            userLocation.setText("Not Available");
        }
        refresh(4000);
    }


    public void refresh(int milliseconds) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateLoc();
            }
        };

        handler.postDelayed(runnable, milliseconds);
    }

}
