package com.example.geoswitch.HelperClasses.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoswitch.NearbyPlacesFragment;
import com.example.geoswitch.R;

import java.util.ArrayList;

public class PlaceCategoryAdapter extends RecyclerView.Adapter<PlaceCategoryAdapter.PlaceCategoryViewHolder> {

    ArrayList<PlaceCategoryRecyclerHelperClass> placeCategories;

    public PlaceCategoryAdapter(ArrayList<PlaceCategoryRecyclerHelperClass> placeCategories) {
        this.placeCategories = placeCategories;
    }

    @NonNull
    @Override
    public PlaceCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_category_card_design, parent, false);
        PlaceCategoryViewHolder placeCategoryViewHolder = new PlaceCategoryViewHolder(view);
        return placeCategoryViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull PlaceCategoryViewHolder holder, int position) {

        PlaceCategoryRecyclerHelperClass placeCategoryRecyclerHelperClass = placeCategories.get(position);

        holder.icon.setImageResource(placeCategoryRecyclerHelperClass.getIcon());
        holder.title.setText(placeCategoryRecyclerHelperClass.getName());
    }


    @Override
    public int getItemCount() {
        return placeCategories.size();
    }


    public static class PlaceCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView icon;
        TextView title;


        public PlaceCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.place_cat_icon);
            title = itemView.findViewById(R.id.place_cat_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new NearbyPlacesFragment(position)).addToBackStack(null).commit();
        }
    }
}
