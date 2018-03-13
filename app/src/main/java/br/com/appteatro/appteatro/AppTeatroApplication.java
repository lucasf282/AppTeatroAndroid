package br.com.appteatro.appteatro;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;

/**
 * Created by hc2mac32 on 05/03/18.
 */

public class AppTeatroApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppEventsLogger.activateApp(this);
    }
}
