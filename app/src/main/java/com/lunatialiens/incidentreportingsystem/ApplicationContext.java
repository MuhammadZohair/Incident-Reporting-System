package com.lunatialiens.incidentreportingsystem;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;


/**
 * The type Application context.
 */
public class ApplicationContext extends Application {

    private static final String TAG = "ApplicationContext";

    private static Context context;

    /**
     * Gets app context.
     *
     * @return the app context
     */
    public static Context getAppContext() {
        return ApplicationContext.context;
    }

    public void onCreate() {
        super.onCreate();
        ApplicationContext.context = getApplicationContext();

        JodaTimeAndroid.init(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}