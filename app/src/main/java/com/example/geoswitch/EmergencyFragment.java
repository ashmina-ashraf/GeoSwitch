package com.example.geoswitch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EmergencyFragment extends Fragment implements View.OnClickListener {

    LinearLayout addContactBtn, addContactOption;
    EditText contactName, phoneNo;
    Button add, sosBtn;
    DBHelperEmergencyContact db;
    ListView listView;
    EmergencyContactListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_emergency, container, false);

        addContactBtn = (LinearLayout) root.findViewById(R.id.linearLayout2);
        addContactOption = (LinearLayout) root.findViewById(R.id.linearLayout3);
        contactName = (EditText) root.findViewById(R.id.contact_name);
        phoneNo = (EditText) root.findViewById(R.id.phn_no);
        add = (Button) root.findViewById(R.id.add);
        listView = (ListView) root.findViewById(R.id.emergency_contact);
        sosBtn = (Button) root.findViewById(R.id.sos);

        updateUI();

        sosBtn.setOnClickListener(this);
        addContactBtn.setOnClickListener(this);
        add.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.linearLayout2) {
            if (addContactOption.getVisibility() == View.GONE) {
                addContactOption.setVisibility(View.VISIBLE);
            } else {
                addContactOption.setVisibility(View.GONE);
            }
        }

        if (v.getId() == R.id.add) {
            String cn = contactName.getText().toString();
            String phn = phoneNo.getText().toString();

            db = new DBHelperEmergencyContact(Global.getGlobalContext());
            Boolean isSuccess = db.addContact(cn, phn);
            if (isSuccess) {
                Toast.makeText(Global.getGlobalContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                addContactOption.setVisibility(View.GONE);
                updateUI();
            } else {
                Toast.makeText(Global.getGlobalContext(), "Failed to add", Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.sos) {
            String message = "URGENT MESSAGE!\n" + Global.getCurrentUser().getUsername() + "is in an emergency situation.\n" +
                    "You are added as their emergency contact.\nCurrent Location:\n";
            ArrayList<String> contactNumbers = EmergencyContact.getContactNumbers();
            int count = 0;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Global.getGlobalContext().checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    for (int i = 0; i < EmergencyContact.getCount(); i++) {
                        sendSMS(contactNumbers.get(i), message);
                        count++;
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                }
            }


            Toast.makeText(Global.getGlobalContext(), "Emergency SMS sent to " + count + " contact(s).",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(String s, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(s, null, message, null, null);
        } catch (Exception e) {
            Toast.makeText(Global.getGlobalContext(), "Failed to send", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUI() {

        DBHelperEmergencyContact db = new DBHelperEmergencyContact(Global.getGlobalContext());
        db.getContacts();

        EmergencyContactListViewAdapter adapter = new EmergencyContactListViewAdapter(Global.getGlobalContext(), EmergencyContact.getEmergencyContacts());
        listView.setAdapter(adapter);

        if (EmergencyContact.getCount() <= 0) {
            sosBtn.setEnabled(false);
        } else {
            sosBtn.setEnabled(true);
        }
    }
}