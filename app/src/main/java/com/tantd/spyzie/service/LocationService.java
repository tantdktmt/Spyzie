package com.tantd.spyzie.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.tantd.spyzie.R;
import com.tantd.spyzie.util.Constants;

public class LocationService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            if (location != null) {
                Log.d(Constants.LOG_TAG, "requestLocationUpdates, lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.LOG_TAG, "onCreate()");
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.LOG_TAG, "onStartCommand()");
        String input = intent.getStringExtra("input");
        createNotificationChannelForAndroidO();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_stat_name)
                .build();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                if (checkPermissions() && isLocationTurnOn()) {
                    Log.d(Constants.LOG_TAG, "i=" + i + " foreground");
                    startForeground(1, notification);
                    requestLocationUpdates();
                } else {
                    Log.d(Constants.LOG_TAG, "i=" + i + " remove foreground");
                    removeLocationUpdates();
                    stopForeground(true);
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            removeLocationUpdates();
            stopSelf();
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, "onDestroy()");
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

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationTurnOn() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(10 * 1000);
        mLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
    }

    private void removeLocationUpdates() {
        mLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }
}
