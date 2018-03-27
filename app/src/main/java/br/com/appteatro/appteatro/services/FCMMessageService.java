package br.com.appteatro.appteatro.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.appteatro.appteatro.Activities.MainActivity;
import br.com.appteatro.appteatro.R;

/**
 * Created by lucasfarias on 10/03/18.
 */

public class FCMMessageService extends FirebaseMessagingService{
    private static final String TAG = "FCM";
    private final String CHANNEL_ID = "AppTeatroNotification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0){
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null){

            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            final RemoteMessage.Notification notification = remoteMessage.getNotification();

            //TODO: verificar forma correta de usar o Channel ID
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.theatear_icon)
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getBody())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            //TODO: verificar forma correta de usar o NotificationId
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(27, mBuilder.build());

            Log.d(TAG, "Message botification body: " + remoteMessage.getNotification().getBody());
        }
    }
}
