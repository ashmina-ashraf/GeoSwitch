package com.example.geoswitch;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AutoSilentAdapter extends RecyclerView.Adapter<AutoSilentAdapter.AutoSilentViewHolder> {

    ArrayList<AutoSilent> autoSilentArrayList;

    public AutoSilentAdapter(ArrayList<AutoSilent> autoSilentArrayList) {
        this.autoSilentArrayList = autoSilentArrayList;
    }

    @NonNull
    @Override
    public AutoSilentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.autosilent_card_design, parent, false);
        AutoSilentAdapter.AutoSilentViewHolder autoSilentViewHolder = new AutoSilentAdapter.AutoSilentViewHolder(view);
        return autoSilentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AutoSilentAdapter.AutoSilentViewHolder holder, int position) {
        AutoSilent autoSilent = autoSilentArrayList.get(position);

        if (isStarted(autoSilent.startDate)) {
            holder.tv1.setText("Started on");
        } else {
            holder.tv1.setText("Starts on");
        }

        String[] formattedDate = formatDate(autoSilent.startDate);
        String[] formattedEndDate = formatDate(autoSilent.endDate);
        holder.day.setText(formattedDate[0]);
        holder.month.setText(formattedDate[1]);
        holder.date.setText(formattedDate[2]);

        holder.taskTitle.setText(autoSilent.getTitle());
        holder.location.setText(autoSilent.getGeofenceCoordinates().getPlace());

        holder.endDate.setText("Ends on:  " +
                formattedEndDate[2] + " " + formattedEndDate[1] + ", " + formattedEndDate[0]);
    }

    @Override
    public int getItemCount() {
        return autoSilentArrayList.size();
    }

    public class AutoSilentViewHolder extends RecyclerView.ViewHolder {

        TextView day, date, month, taskTitle, location, endDate, tv1;
        ImageView options;

        public AutoSilentViewHolder(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.autosilent_tv1);
            day = itemView.findViewById(R.id.autosilent_day);
            date = itemView.findViewById(R.id.autosilent_date);
            month = itemView.findViewById(R.id.autosilent_month);
            taskTitle = itemView.findViewById(R.id.autosilent_title);
            location = itemView.findViewById(R.id.autosilent_location);
            endDate = itemView.findViewById(R.id.autosilent_enddate);
            options = itemView.findViewById(R.id.autosilent_options);

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu reminderMenu = new PopupMenu(Global.getGlobalContext(), v);
                    reminderMenu.inflate(R.menu.reminder_popup_menu);
                    reminderMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int position = getAdapterPosition();
                            AutoSilent autoSilent = new AutoSilent(autoSilentArrayList.get(position));

                            switch (item.getItemId()) {
                                case R.id.rem_update:
                                    Toast.makeText(Global.getGlobalContext(), "update",
                                            Toast.LENGTH_SHORT).show();
                                    break;

                                case R.id.rem_delete:
                                    Toast.makeText(Global.getGlobalContext(), "Del",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return false;
                        }
                    });
                    reminderMenu.show();
                }
            });
        }
    }

    public Boolean isStarted(String startDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date currentDate = Calendar.getInstance().getTime();
            Date sDate = sdf.parse(startDate);

            if (sDate.compareTo(currentDate) <= 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String[] formatDate(String startDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date sDate = sdf.parse(startDate);
            String stringDate = sDate.toString();
            Log.d("Autodate", stringDate);
            String[] splittedDate = stringDate.split(" ");

            return splittedDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}