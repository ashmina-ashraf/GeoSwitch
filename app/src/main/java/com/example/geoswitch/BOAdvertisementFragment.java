package com.example.geoswitch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class BOAdvertisementFragment extends Fragment {

    ViewGroup root;
    LinearLayout addAd;
    RecyclerView adRecycler;
    DBHelperBusinessRegister db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_bo_advertisement, container, false);

        addAd = root.findViewById(R.id.bo_add_adv);
        adRecycler = (RecyclerView) root.findViewById(R.id.bo_ad_view);

        db = new DBHelperBusinessRegister(Global.getGlobalContext());
        advertisementRecycler();

        addAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new BOAddAdvertisementFragment());
            }
        });

        return root;
    }

    private void advertisementRecycler() {
        adRecycler.setHasFixedSize(true);
        adRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        ArrayList<Advertisement> adList = db.getBOAdvertisements();
        UserAdvertisementAdapter adapter = new UserAdvertisementAdapter(adList);
        adRecycler.setAdapter(adapter);
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bo_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}