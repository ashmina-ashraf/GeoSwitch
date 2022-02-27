package com.example.geoswitch;

import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.geoswitch.HelperClasses.HomeAdapter.PlaceCategoryAdapter;
import com.example.geoswitch.HelperClasses.HomeAdapter.PlaceCategoryRecyclerHelperClass;

import java.util.ArrayList;

public class ToDoFragment extends Fragment {

    LinearLayout addReminderBtn;
    RecyclerView reminderRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_to_do, container, false);

        addReminderBtn = (LinearLayout) root.findViewById(R.id.add_reminder);
        reminderRecycler = (RecyclerView) root.findViewById(R.id.reminder_recycler_view);
        updateUI();

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("to")) {
                AddNewReminderFragment addNewReminderFragment = new AddNewReminderFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("Set location", "1");
                addNewReminderFragment.setArguments(bundle1);
                addNewReminderFragment.show(getParentFragmentManager(), "Reminder");
            }
        }

        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewReminderFragment addNewReminderFragment = new AddNewReminderFragment();
                addNewReminderFragment.show(getParentFragmentManager(), "Reminder");
            }
        });
        return root;
    }

    private void recycler() {
        reminderRecycler.setHasFixedSize(true);
        reminderRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false));

        DBHelperGeofence db = new DBHelperGeofence(Global.getGlobalContext());
        ArrayList<Reminder> remindersList = db.getReminders();

        ReminderAdapter adapter = new ReminderAdapter(remindersList);
        reminderRecycler.setAdapter(adapter);
    }

    private void updateUI() {
        recycler();
    }
}