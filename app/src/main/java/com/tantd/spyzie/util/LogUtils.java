package com.tantd.spyzie.util;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by tantd on 8/22/2017.
 */
public class LogUtils {

    private static boolean isDebug = true;
    private static String tag = "NO TAG";

    public static void init(boolean isDebug, String tag) {
        LogUtils.isDebug = isDebug;
        LogUtils.tag = tag;
    }

    public static void i(Object object) {
        if (isDebug) {
            Log.i(tag, String.valueOf(object));
        }
    }

    public static void d(Object object) {
        if (isDebug) {
            Log.d(tag, String.valueOf(object));
        }
    }

    public static void e(Object... object) {
        if (isDebug) {
            Log.e(tag, Arrays.toString(object));
        }
    }

    public static void w(Object object) {
        if (isDebug) {
            Log.w(tag, String.valueOf(object));
        }
    }
}