package com.tantd.spyzie.ui.widget;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;

import com.tantd.spyzie.data.device.AppDeviceDataManager;
import com.tantd.spyzie.util.Constants;

/**
 * Created by tantd on 3/27/2020.
 */
public class MyAppWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        Log.d(Constants.LOG_TAG, "[MyAppWidget] onEnabled()");
        AppDeviceDataManager.getInstance().startFetchingDeviceData();
    }
}

