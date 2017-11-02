package example.com.readit.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import example.com.readit.R;
import example.com.readit.ui.home.HomeActivity;

/**
 * Created by FamilyPC on 11/1/2017.
 */

@SuppressWarnings("ALL")
public class ReaditFirebaseMessagingService extends FirebaseMessagingService {

    private static final String LOG_TAG = ReaditFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(LOG_TAG, "onMessageReceived");

        String messageBody = remoteMessage.getNotification().getBody();
        Log.d(LOG_TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        sendNotification(messageBody);
    }

    private void sendNotification(String messageBody) {
        Log.d(LOG_TAG, "Sending notification");
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Readit")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        Log.d(LOG_TAG, "notification send");
    }
}
