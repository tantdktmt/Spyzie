package com.tantd.spyzie;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.androidnetworking.AndroidNetworking;
import com.tantd.spyzie.core.ObjectBox;
import com.tantd.spyzie.di.component.AppComponent;
import com.tantd.spyzie.di.component.DaggerAppComponent;
import com.tantd.spyzie.di.component.ServiceComponent;
import com.tantd.spyzie.di.module.AppModule;
import com.tantd.spyzie.di.module.ServiceModule;

/**
 * Created by tantd on 2/7/2020.
 */
public class SpyzieApplication extends Application {

    public static final String TAG = SpyzieApplication.class.getSimpleName();
    private static SpyzieApplication mInstance;

    private AppComponent mAppComponent;
    private ServiceComponent mServiceComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ObjectBox.init(this);
        AndroidNetworking.initialize(getApplicationContext());
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public static synchronized SpyzieApplication getInstance() {
        return mInstance;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public ServiceComponent createServiceComponent(ServiceModule module) {
        mServiceComponent = mAppComponent.plus(module);
        return mServiceComponent;
    }

    public void releaseServiceComponent() {
        mServiceComponent = null;
    }

    public ServiceComponent getServiceComponent() {
        return mServiceComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
