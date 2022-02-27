package com.example.geoswitch;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdImageAdapter extends RecyclerView.Adapter<UserAdImageAdapter.UserAdImageViewHolder> {

    ArrayList<Bitmap> imageList;

    public UserAdImageAdapter(ArrayList<Bitmap> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public UserAdImageAdapter.UserAdImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_add_img_card_design, parent, false);
        UserAdImageAdapter.UserAdImageViewHolder userAdImageViewHolder = new UserAdImageAdapter.UserAdImageViewHolder(view);
        return userAdImageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdImageAdapter.UserAdImageViewHolder holder, int position) {
        Bitmap bitmapImg = imageList.get(position);
        holder.img.setImageBitmap(bitmapImg);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class UserAdImageViewHolder extends RecyclerView.ViewHolder {

        ImageView img;

        public UserAdImageViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.user_ad_image1);
        }
    }
}
