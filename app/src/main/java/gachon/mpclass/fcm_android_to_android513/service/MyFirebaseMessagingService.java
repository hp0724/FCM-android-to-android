package gachon.mpclass.fcm_android_to_android513.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;

import gachon.mpclass.fcm_android_to_android513.MainActivity;
import gachon.mpclass.fcm_android_to_android513.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final static String TAG = "FCM_MESSAGE";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "one-channel";
        String channelName = "My Channel One1";
        String channelDescription = "My Channel One Description";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                //??????
                .setContentTitle(remoteMessage.getData().get("title"))
                //??????
                .setContentText(remoteMessage.getData().get("body"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        try {
            URL url = new URL(remoteMessage.getData().get("largeIcon"));
            //????????? ??????
            Bitmap bigIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            notificationBuilder.setLargeIcon(bigIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(333 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

}
//        super.onMessageReceived(remoteMessage);
//        if (remoteMessage.getNotification() != null) {
//            String body = remoteMessage.getNotification().getBody();
//            Log.d(TAG, "Notification Body: " + body);
//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel")
//                    .setSmallIcon(R.mipmap.ic_launcher) // ?????? ????????? ?????? ??? ?????????.
//                    .setContentTitle(getString(R.string.app_name)) // ?????? ????????? ?????? ??? ?????????
//                    .setContentText(body); // Firebase Console ?????? ???????????? ????????? ???????????????
//
//            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
//            notificationManagerCompat.notify(0x1001, notificationBuilder.build());
////        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        /* ??????????????? ??????????????? ???????????? ?????? */
//
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "From: " + remoteMessage.getFrom());
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//
//            String messageBody = remoteMessage.getNotification().getBody();
//            String messageTitle = remoteMessage.getNotification().getTitle();
//        }


//    public static String getToken(Context context) {
//        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
//    }




