package com.example.geoswitch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdvertisementAdapter extends RecyclerView.Adapter<UserAdvertisementAdapter.AdViewHolder> {

    ArrayList<Advertisement> adList;

    public UserAdvertisementAdapter(ArrayList<Advertisement> adList) {
        this.adList = adList;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_ad_view_card_design, parent, false);
        UserAdvertisementAdapter.AdViewHolder adViewHolder = new UserAdvertisementAdapter.AdViewHolder(view);
        return adViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        Advertisement ad = adList.get(position);

        holder.offer.setText(ad.getOffer());
        holder.tagline.setText(ad.getTagline());
        holder.businessName.setText(ad.getBusinessName());

        holder.imgRecycler.setHasFixedSize(true);
        holder.imgRecycler.setLayoutManager(new LinearLayoutManager(Global.getGlobalContext(),
                LinearLayoutManager.HORIZONTAL, false));

        ArrayList<Bitmap> imageList = new ArrayList<>();
        imageList.add(ad.img1);
        imageList.add(ad.img2);
        imageList.add(ad.img3);

        UserAdImageAdapter adapter = new UserAdImageAdapter(imageList);
        holder.imgRecycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        TextView offer, tagline, businessName;
        RecyclerView imgRecycler;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.user_ad_container);
            offer = itemView.findViewById(R.id.user_ad_offer);
            tagline = itemView.findViewById(R.id.user_ad_tagline);
            businessName = itemView.findViewById(R.id.user_ad_busName);
            imgRecycler = itemView.findViewById(R.id.user_ad_img);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = Global.getGlobalContext();

                    int position = getAdapterPosition();
                    Advertisement ad = adList.get(position);
                    Log.d("testing", ad.offer);
                    GeofenceCoordinates adLocation = ad.getGeofenceCoordinates();
                    Log.d("testing", String.valueOf(ad.getGeofenceCoordinates().getLatitude()));
                    Log.d("testing", "current" + CurrentLocation.getCurrentLocation(context));
                    Log.d("testing", "dest" + adLocation.getPlace());

//                    Uri uri = Uri.parse("https://www.google.co.in/maps/dir/;
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    intent.setPackage("com.google.android.apps.maps");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                }
            });

        }
    }
}
