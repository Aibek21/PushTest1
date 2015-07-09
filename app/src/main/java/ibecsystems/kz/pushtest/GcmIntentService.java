package ibecsystems.kz.pushtest;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by aibek on 19.05.15.
 */
public class GcmIntentService extends IntentService {

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = GcmIntentService.class.getName();

    public GcmIntentService() {
        super("GcmIntentService");
    }

    public final String PUSH_PREFERENCES = "PUSH_PREFERENCES";
    SharedPreferences sharedPreferences;

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);


        if (messageType!=null)
        Log.e("messageType", messageType);
        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                try {
                    sendNotification(extras);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                try {
                    sendNotification(extras);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                try {
                    sendNotification(extras);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(Bundle msg) throws IOException {
        int id=1;
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Log.e("PUSH", msg.toString());
        //String message = msg.getString("message");
        sharedPreferences = getSharedPreferences(PUSH_PREFERENCES, Context.MODE_PRIVATE);
        //String type = msg.getString("type");
        Intent actionIntent = getActionIntent(msg);
        PendingIntent contentIntent = PendingIntent.getActivity
                (this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(msg.toString())
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(contentIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg.toString()))
                    .setAutoCancel(true);


            mNotificationManager.notify(id, mBuilder.build());

    }

    private Intent getActionIntent(Bundle msg) {
        Intent actionIntent = new Intent(getApplicationContext(), MainActivity.class);

        return actionIntent;
    }


}


