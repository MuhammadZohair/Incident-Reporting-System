package com.lunatialiens.incidentreportingsystem.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.utils.AppUtils;
import com.lunatialiens.incidentreportingsystem.views.fragments.HomeFragment;
import com.lunatialiens.incidentreportingsystem.views.fragments.MapsFragment;
import com.lunatialiens.incidentreportingsystem.views.fragments.SettingsFragment;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DashboardActivity";

    private boolean doubleBackToExitPressedOnce = false;

    private HomeFragment homeFragment;
    private MapsFragment mapsFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        homeFragment = new HomeFragment();
        mapsFragment = new MapsFragment();
        settingsFragment = new SettingsFragment();

        loadFragment(mapsFragment);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.navigation_dashboard:
                loadFragment(homeFragment);
                break;
            case R.id.navigation_settings:
                loadFragment(settingsFragment);
                break;
            case R.id.navigation_maps:
                loadFragment(mapsFragment);
                break;
            case R.id.navigation_logout:
                AppUtils.success(getApplicationContext(), "Logout Successful");
                i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK twice to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFragment(mapsFragment);
    }
}
