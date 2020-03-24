package com.tantd.spyzie.core;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tantd.spyzie.R;

/**
 * Created by tantd on 2/7/2020.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.Callback {

    public static final int ANIM_NONE = 0;
    public static final int ANIM_BOTTOM_TO_TOP = 1;
    public static final int ANIM_TOP_TO_BOTTOM = 2;
    public static final int ANIM_RIGHT_TO_LEFT = 3;
    public static final int ANIM_LEFT_TO_RIGHT = 4;
    public static final int ANIM_FADE_IN_FADE_OUT = 5;
    public static final int ANIM_DEFAULT = ANIM_LEFT_TO_RIGHT;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFragmentAttached() {
    }

    @Override
    public void onFragmentDetached(String tag) {
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int messageId) {
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message, int position) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(position, 0, 0);
        toast.show();
    }

    public void showProgressDialog(String message) {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.show();
            } else {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(message);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Using try catch to catch the case: The activity is not running but still show the dialog.
    }

    public void showProgressDialog(int messageId) {
        showProgressDialog(getString(messageId));
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    public void startActivity(Class<?> cls) {
        startActivity(cls, ANIM_DEFAULT);
    }

    public void startActivity(Intent intent) {
        startActivity(intent, ANIM_DEFAULT);
    }

    public void startActivity(Class<?> cls, int animationType) {
        startActivity(new Intent(this, cls));
        startTransition(animationType);
    }

    public void startActivity(Intent intent, int animationType) {
        super.startActivity(intent);
        startTransition(animationType);
    }

    public void startActivityForResult(Intent intent, int requestCode, int animationType) {
        super.startActivityForResult(intent, requestCode);
        startTransition(animationType);
    }

    public void finish() {
        super.finish();
        startTransition(ANIM_DEFAULT);
    }

    public void finish(int animationType) {
        super.finish();
        startTransition(animationType);
    }

    public void startTransition(int animationType) {
        switch (animationType) {
            case ANIM_BOTTOM_TO_TOP:
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                break;
            case ANIM_TOP_TO_BOTTOM:
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case ANIM_RIGHT_TO_LEFT:
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case ANIM_LEFT_TO_RIGHT:
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case ANIM_FADE_IN_FADE_OUT:
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}