package edu.uw.cwc8.yama;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Broadcast Receiver from the lecture
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Log.v("RECEIVER", intent.toString());

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage msg : messages){
            Log.v("RECEIVER", msg.getOriginatingAddress() + ": " + msg.getMessageBody());
            Toast t = Toast.makeText(context, msg.getOriginatingAddress() + ": " + msg.getMessageBody(), Toast.LENGTH_SHORT);
            t.show();
        }

    }
}