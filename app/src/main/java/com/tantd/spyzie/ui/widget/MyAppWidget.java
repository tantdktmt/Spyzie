package com.tantd.spyzie.ui.widget;

import android.appwidget.AppWidgetManager;
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
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onEnabled()");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onUpdate()");
        }
    }

    @Override
    public void onDisabled(Context context) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onDisabled()");
        }
        Intent serviceIntent = new Intent(SpyzieApplication.getInstance(), MainService.class);
        SpyzieApplication.getInstance().stopService(serviceIntent);
    }
}

