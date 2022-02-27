package com.example.geoswitch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EmergencyContactListViewAdapter extends ArrayAdapter<EmergencyContact> {

    ArrayList<EmergencyContact> emergencyContacts = new ArrayList<>();
    Context context;
    TextView name, phone;
    ImageView user, del;
    DBHelperEmergencyContact db;

    public EmergencyContactListViewAdapter(Context context, ArrayList<EmergencyContact> list) {
        super(context, R.layout.emergency_contact_list_row, list);
        this.context = context;
        this.emergencyContacts = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.emergency_contact_list_row, parent, false);
        }

        name = (TextView) convertView.findViewById(R.id.contact_name);
        phone = (TextView) convertView.findViewById(R.id.contact_number);
        user = (ImageView) convertView.findViewById(R.id.icon_contact);
        del = (ImageView) convertView.findViewById(R.id.icon_delete);

        name.setText(emergencyContacts.get(position).getContactName());
        phone.setText(emergencyContacts.get(position).getPhone());

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DBHelperEmergencyContact(Global.getGlobalContext());

                db.deleteContact(emergencyContacts.get(position).getPhone());
                db.getContacts();
            }
        });

        return convertView;
    }
}
