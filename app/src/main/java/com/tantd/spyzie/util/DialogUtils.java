package com.tantd.spyzie.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.tantd.spyzie.R;

/**
 * Created by tantd on 8/22/2017.
 */
public class DialogUtils {

    public static void showMessageDialog(Context context, int resMessage, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(resMessage)
                .setPositiveButton(android.R.string.ok, okListener);
        builder.create().show();
    }

    public static void showMessageDialog(Context context, int resMessage) {
        showMessageDialog(context, resMessage, null);
    }

    public static void showConfirmDialog(Context context, int resMessage, int resNegative, int resPositive,
                                         DialogInterface.OnClickListener cancelListener, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(resMessage)
                .setNegativeButton(resNegative, cancelListener)
                .setPositiveButton(resPositive, okListener)
                .setCancelable(false);
        builder.create().show();
    }

    public static void showConfirmDialog(Context context, int resMessage, int resNegative, int resPositive, DialogInterface.OnClickListener okListener) {
        showConfirmDialog(context, resMessage, resNegative, resPositive, null, okListener);
    }
}