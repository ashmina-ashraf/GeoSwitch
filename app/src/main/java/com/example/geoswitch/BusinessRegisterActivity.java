package com.example.geoswitch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import maes.tech.intentanim.CustomIntent;

public class

BusinessRegisterActivity extends AppCompatActivity {

    EditText businessName, email, pswd, confirmPswd, businessType, location;
    Button register;
    DBHelperBusinessRegister db;
    Location selectedLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_register);

        DBHelper createTables = new DBHelper(this);
        db = new DBHelperBusinessRegister(this);

        businessName = (EditText) findViewById(R.id.business_name);
        email = (EditText) findViewById(R.id.business_email);
        pswd = (EditText) findViewById(R.id.business_pswd);
        confirmPswd = (EditText) findViewById(R.id.business_confirm_pswd);
        businessType = (EditText) findViewById(R.id.business_type);
        location = (EditText) findViewById(R.id.business_location);

        if (getIntent().hasExtra("Location")) {

            Double latitude = getIntent().getExtras().getDouble("Latitude");
            Double longitude = getIntent().getExtras().getDouble("Longitude");
            selectedLoc = new Location(latitude, longitude);

            location.setText(selectedLoc.getPlace());
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Global.getGlobalContext(), AddGeofenceMap.class);
                i.putExtra("to", "bo_register");
                startActivity(i);
            }
        });

        register = (Button) findViewById(R.id.business_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_ = email.getText().toString();
                String name = businessName.getText().toString();
                String pwd = pswd.getText().toString();
                String confirmPwd = confirmPswd.getText().toString();
                String type = businessType.getText().toString();

                BORegister(email_, name, pwd, confirmPwd, type, selectedLoc);
            }
        });
    }

    private void BORegister(String email_, String name, String pwd, String confirmPwd, String type, Location loc) {

        if (email_.equals("") || name.equals("") || pwd.equals("") || confirmPwd.equals("") || type.equals("") || loc.equals("")) {
            Toast.makeText(this, "No fields should be empty", Toast.LENGTH_SHORT).show();
        } else if (!pwd.equals(confirmPwd)) {
            Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show();
        } else {
            Boolean ifExists = db.checkBusinessOwnerExists(email_);

            if (ifExists) {
                Toast.makeText(this, "This email is already registered. Try logging in.", Toast.LENGTH_SHORT).show();
                goToLoginPage();
            } else {

                Boolean res = db.insertData(email_, name, pwd, type, loc);
                if (res == true) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    goToLoginPage();
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void goToLoginPage() {
        Intent i = new Intent(this, UserLoginSignup.class);
        i.putExtra("tab", "0");
        startActivity(i);

        CustomIntent.customType(this, "right-to-left");
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("email", email.getText().toString());
        outState.putString("businessName", businessName.getText().toString());
        outState.putString("pswd", pswd.getText().toString());
        outState.putString("confirmPswd", confirmPswd.getText().toString());
        outState.putString("businessType", businessType.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        email.setText(savedInstanceState.getString("email"));
        businessName.setText(savedInstanceState.getString("businessName"));
        businessType.setText(savedInstanceState.getString("businessType"));
        pswd.setText(savedInstanceState.getString("pswd"));
        confirmPswd.setText(savedInstanceState.getString("confirmPswd"));
    }
}