package com.example.geoswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

public class UserLoginSignup extends AppCompatActivity {

    TabLayout tablayout;
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_signup);

        tablayout = (TabLayout) findViewById(R.id.tab_layout);
        viewpager = (ViewPager) findViewById(R.id.view_pager);

        tablayout.addTab(tablayout.newTab().setText("Login"));
        tablayout.addTab(tablayout.newTab().setText("Sign Up"));
        tablayout.setTabGravity(tablayout.GRAVITY_FILL);

        final LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), this, tablayout.getTabCount());
        viewpager.setAdapter(loginAdapter);

        viewpager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tablayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpager));

        String tab = getIntent().getExtras().getString("tab");
        switch (tab) {
            case "0":
                tablayout.getTabAt(0).select();
                break;
            case "1":
                tablayout.getTabAt(1).select();
                break;
            default:
                break;
        }
    }
}