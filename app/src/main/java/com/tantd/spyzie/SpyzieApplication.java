package com.tantd.spyzie;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by tantd on 2/7/2020.
 */
public class SpyzieApplication extends Application {

    public static final String TAG = SpyzieApplication.class.getSimpleName();
    private static SpyzieApplication mInstance;

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AndroidNetworking.initialize(getApplicationContext());
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public static synchronized SpyzieApplication getInstance() {
        return mInstance;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
