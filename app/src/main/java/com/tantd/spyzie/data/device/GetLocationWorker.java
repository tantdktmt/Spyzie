package com.tantd.spyzie.data.device;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tantd.spyzie.util.Constants;

public class GetLocationWorker extends Worker {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public GetLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int multiple = getInputData().getInt("start_input", -1);
        int count = getInputData().getInt("count", -1);
        Log.d(Constants.LOG_TAG, "[FilterWorker] doWork, startValue=" + multiple + ", loop count=" + count);

        try {
            for (int i = 1; i <= count; i++) {
                multiple += i;
                Log.d(Constants.LOG_TAG, "[FilterWorker " + hashCode() + "] doWork, i=" + i + ", sum=" + multiple);
                showToast("i=" + i + ", sum=" + multiple);
                Thread.sleep(1000);
            }
            return Result.success(new Data.Builder().putInt("multiple", multiple).build());
        } catch (Exception e) {
            return Result.failure();
        }
    }

    private void showToast(String message) {
        mHandler.post(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }
}
