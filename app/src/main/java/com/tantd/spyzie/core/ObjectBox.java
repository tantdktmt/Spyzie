package com.tantd.spyzie.core;

import android.content.Context;
import android.util.Log;

import com.tantd.spyzie.data.db.MyObjectBox;
import com.tantd.spyzie.util.Constants;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class ObjectBox {

    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();

        Log.d(Constants.LOG_TAG, String.format("Using ObjectBox %s (%s)",
                BoxStore.getVersion(), BoxStore.getVersionNative()));
        new AndroidObjectBrowser(boxStore).start(context.getApplicationContext());
    }

    public static BoxStore get() {
        return boxStore;
    }
}
