package com.tantd.spyzie.data.device.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.data.model.AccessTokenResponse;
import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.rx.SchedulerProvider;

import javax.inject.Inject;

public class RefreshTokenWorker extends Worker {

    public static final String REFRESH_TOKEN_WORK_REQUEST = "REFRESH_TOKEN_WORK_REQUEST";

    @Inject
    ApiManager mApiManager;
    @Inject
    SchedulerProvider mSchedulerProvider;

    private static final String DEBUG_SUB_TAG = "[" + RefreshTokenWorker.class.getSimpleName() + "] ";

    public RefreshTokenWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SpyzieApplication.getInstance().getServiceComponent().inject(this);

        mApiManager.refreshAccessToken().subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(response -> handleSuccess(response),
                        error -> handleSendingError(error));
        return Result.success();
    }

    private void handleSuccess(AccessTokenResponse response) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "handleSuccess, response=" + response);
        }
        mApiManager.storeAccessToken(response);
    }

    private void handleSendingError(Throwable error) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "handleSendingError: " + error);
        }
    }
}
