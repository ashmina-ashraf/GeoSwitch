package com.example.geoswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button login, signup, bus_owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.login_btn);
        signup = (Button) findViewById(R.id.signup_btn);
        bus_owner = (Button) findViewById(R.id.busowner_btn);

        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        bus_owner.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.login_btn:
                i = new Intent(getApplicationContext(), UserLoginSignup.class);
                i.putExtra("tab", "0");
                startActivity(i);
                CustomIntent.customType(this, "left-to-right");
                finish();
                break;

            case R.id.signup_btn:
                i = new Intent(getApplicationContext(), UserLoginSignup.class);
                i.putExtra("tab", "1");
                startActivity(i);
                CustomIntent.customType(this, "left-to-right");
                finish();
                break;

            case R.id.busowner_btn:
                i = new Intent(getApplicationContext(), BusinessRegisterActivity.class);
                startActivity(i);
                CustomIntent.customType(this, "left-to-right");
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}