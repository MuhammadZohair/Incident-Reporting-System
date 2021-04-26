package com.lunatialiens.incidentreportingsystem.views.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.repository.CurrentDatabase;
import com.lunatialiens.incidentreportingsystem.repository.FirebaseDatabaseHelper;
import com.lunatialiens.incidentreportingsystem.utils.AppUtils;
import com.lunatialiens.incidentreportingsystem.utils.Constants;
import com.lunatialiens.incidentreportingsystem.utils.NetworkStateReceiver;

/**
 * The type Splash activity.
 */
public class SplashActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private static final String TAG = "SplashActivity";
    private Snackbar snackbar;
    private NetworkStateReceiver networkStateReceiver;

    private boolean opened = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View parentLayout = findViewById(android.R.id.content);
        snackbar = Snackbar.make(parentLayout, "Not connected to Internet! Please turn on wifi or mobile data", Snackbar.LENGTH_INDEFINITE);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);

        registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (networkStateReceiver != null)
            unregisterReceiver(networkStateReceiver);
    }

    private void splashScreenTime() {

//        FirebaseDatabaseHelper.createMealItem(new Meal("Chicken Karhai", 120));

        Log.e(TAG, "In Splash Screen");
        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */
        new Handler().postDelayed(() -> {

            String email = AppUtils.getDataFromSharedPrefs(getApplicationContext(), Constants.EMAIL);
            String password = AppUtils.getDataFromSharedPrefs(getApplicationContext(), Constants.PASSWORD);

            CurrentDatabase.setCurrentPublicUser(FirebaseDatabaseHelper.getPublicUserByEmailAndPassword(email, password));
            if (CurrentDatabase.getCurrentPublicUser() != null) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();

        }, 6000);
    }

    @Override
    public void networkAvailable() {
        snackbar.dismiss();
        if (opened) {
            new FirebaseDatabaseHelper();
            splashScreenTime();
            opened = false;
        }
    }

    @Override
    public void networkUnavailable() {
        snackbar.show();
    }
}
