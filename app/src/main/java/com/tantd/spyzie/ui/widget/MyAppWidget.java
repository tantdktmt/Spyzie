package com.tantd.spyzie.ui.widget;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.service.MainService;
import com.tantd.spyzie.util.Constants;

/**
 * Created by tantd on 3/27/2020.
 */
public class MyAppWidget extends AppWidgetProvider {

    private static final String DEBUG_SUB_TAG = "[" + MyAppWidget.class.getSimpleName() + "] ";

    @Override
    public void onEnabled(Context context) {
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onEnabled()");
        Intent serviceIntent = new Intent(SpyzieApplication.getInstance(), MainService.class);
        SpyzieApplication.getInstance().startService(serviceIntent);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onDisabled()");
        Intent serviceIntent = new Intent(SpyzieApplication.getInstance(), MainService.class);
        SpyzieApplication.getInstance().stopService(serviceIntent);
    }
}

