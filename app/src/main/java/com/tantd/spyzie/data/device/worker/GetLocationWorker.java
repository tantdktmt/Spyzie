package com.tantd.spyzie.data.device.worker;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.tantd.spyzie.util.CommonUtils;
import com.tantd.spyzie.util.Constants;

public class GetLocationWorker extends Worker {

    private static final int UPDATE_INTERVAL = 5 * 1000;
    private static final int FASTEST_UPDATE_INTERVAL = 5 * 1000;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            if (location != null) {
                Log.d(Constants.LOG_TAG, "[GetLocationWorker] requestLocationUpdates, lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
            }
        }
    };

    public GetLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        int multiple = getInputData().getInt("start_input", -1);
        int count = getInputData().getInt("count", -1);
        Log.d(Constants.LOG_TAG, "[GetLocationWorker] doWork, startValue=" + multiple + ", loop count=" + count);


        if (!CommonUtils.checkPermissions(getApplicationContext())) {
            Log.d(Constants.LOG_TAG, "[GetLocationWorker] dont have location permission");
        } else if (!CommonUtils.isLocationTurnOn(getApplicationContext())) {
            Log.d(Constants.LOG_TAG, "[GetLocationWorker] location is turn off");
        } else {
            requestNewLocationData();
        }

        try {
            for (int i = 1; i <= count; i++) {
                multiple += i;
                Log.d(Constants.LOG_TAG, "[GetLocationWorker " + hashCode() + "] doWork, i=" + i + ", sum=" + multiple);
                Thread.sleep(1000);
            }
            return Result.success(new Data.Builder().putInt("multiple", multiple).build());
        } catch (Exception e) {
            return Result.failure();
        }
    }

    private void requestNewLocationData() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
    }
}
