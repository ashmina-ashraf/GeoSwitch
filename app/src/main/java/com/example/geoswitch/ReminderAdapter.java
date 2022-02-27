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

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    ArrayList<Reminder> remindersList;

    public ReminderAdapter(ArrayList<Reminder> remindersList) {
        this.remindersList = remindersList;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_card_design, parent, false);
        ReminderAdapter.ReminderViewHolder reminderViewHolder = new ReminderAdapter.ReminderViewHolder(view);
        return reminderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = remindersList.get(position);

        if (isStarted(reminder.getStartDate())) {
            holder.tv1.setText("Started on");
//            holder.status.setText("Ongoing");
        } else {
            holder.tv1.setText("Starts on");
//            holder.status.setText("Upcoming");
        }

        String[] formattedDate = formatDate(reminder.getStartDate());
        holder.day.setText(formattedDate[0]);
        holder.month.setText(formattedDate[1]);
        holder.date.setText(formattedDate[2]);
        holder.taskTitle.setText(reminder.getTask());
        holder.taskDetails.setText(reminder.getDetails());
        holder.location.setText(reminder.getGeofenceCoordinates().getPlace());

        String[] formattedEndDate = formatDate(reminder.getEndDate());
        holder.endDate.setText("Ends on:  " +
                formattedEndDate[2] + " " + formattedEndDate[1] + ", " + formattedEndDate[0]);
    }

    @Override
    public int getItemCount() {
        return remindersList.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {

        TextView day, date, month, taskTitle, taskDetails, location, endDate, status, tv1;
        ImageView options;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.tv1);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
            taskTitle = itemView.findViewById(R.id.task);
            taskDetails = itemView.findViewById(R.id.details);
            location = itemView.findViewById(R.id.reminder_location);
            endDate = itemView.findViewById(R.id.cdate);
//            status = itemView.findViewById(R.id.status);
            options = itemView.findViewById(R.id.options);

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu reminderMenu = new PopupMenu(Global.getGlobalContext(), v);
                    reminderMenu.inflate(R.menu.reminder_popup_menu);
                    reminderMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int position = getAdapterPosition();
                            Reminder reminder = new Reminder(remindersList.get(position));

                            switch (item.getItemId()) {
                                case R.id.rem_update:
                                    Toast.makeText(Global.getGlobalContext(), reminder.getTask(),
                                            Toast.LENGTH_SHORT).show();
                                    break;

                                case R.id.rem_delete:
                                    Toast.makeText(Global.getGlobalContext(), reminder.getTask() + "Del",
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
