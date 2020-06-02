package com.tantd.spyzie.data.db;

import android.content.Context;
import android.util.Log;

import com.tantd.spyzie.data.MyObjectBox;
import com.tantd.spyzie.util.Constants;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public final class ObjectBox {

    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();

        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, String.format("Using ObjectBox %s (%s)",
                    BoxStore.getVersion(), BoxStore.getVersionNative()));
        }
        new AndroidObjectBrowser(boxStore).start(context.getApplicationContext());
    }

    public static BoxStore get() {
        return boxStore;
    }
}
