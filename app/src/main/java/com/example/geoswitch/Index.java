package com.example.geoswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import maes.tech.intentanim.CustomIntent;

public class Index extends AppCompatActivity {
    String[] session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        SessionManagement sessionManagement = new SessionManagement(this);
        session = sessionManagement.getSession();
        if (Integer.parseInt(session[0]) != -1) {
            Log.d("testing", session[0]);
            goToHomePage();
        } else {
            Log.d("testing", "main");
            goToMainActivity();
        }
    }


    private void goToMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        CustomIntent.customType(this, "left-to-right");
    }

    private void goToHomePage() {
        if (Integer.parseInt(session[0]) == 1) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        } else if (Integer.parseInt(session[0]) == 2) {
            startActivity(new Intent(getApplicationContext(), BOHomeActivity.class));
        }
        CustomIntent.customType(this, "fadeout-to-fadein");
    }
}