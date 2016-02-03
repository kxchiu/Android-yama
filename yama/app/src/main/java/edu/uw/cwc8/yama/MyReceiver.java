package edu.uw.cwc8.yama;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Broadcast Receiver from the lecture
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Log.v("Receiver", intent.toString());

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage msg : messages){
            Log.v("Receiver", msg.getOriginatingAddress() + ": " + msg.getMessageBody());
            //Toast t = Toast.makeText(context, msg.getOriginatingAddress() + ": " + msg.getMessageBody(), Toast.LENGTH_SHORT);
            //t.show();

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            Boolean autoReply = sharedPref.getBoolean("autoReply", false);

            //if auto-reply is on, sends customized auto reply message
            if(autoReply){
                String autoMessage = sharedPref.getString("autoReplyMessage", "empty");
                Log.v("Receiver", "Auto-Reply: "+autoReply.toString() + " to " + autoMessage);
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(msg.getOriginatingAddress(), null, autoMessage, null, null);
            }

            //create the notification when receiving a new message
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("New Message from " + msg.getOriginatingAddress())
                    .setContentText(msg.getMessageBody())
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }

    }
}