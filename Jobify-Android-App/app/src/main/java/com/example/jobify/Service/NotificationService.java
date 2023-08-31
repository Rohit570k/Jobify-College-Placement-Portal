package com.example.jobify.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.example.jobify.Models.User;
import com.example.jobify.R;
import com.example.jobify.UI.Myjob.AppliedJobActivity;
import com.example.jobify.Utils.UtilService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";
    public static final  String CHANNEL_ID = "Application Update"; // Create a unique channel ID


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.i(TAG, "From: " + remoteMessage);

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Uri imageUri =  remoteMessage.getNotification().getImageUrl();

            showNotification(title,body,imageUri);

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private  void showNotification(String title , String body, Uri imageUri) {

        Intent activityIntent = new Intent(this, AppliedJobActivity.class);
        PendingIntent pi = PendingIntent.getActivity(
                this,
                0,
                activityIntent,
                PendingIntent.FLAG_IMMUTABLE);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create notification channel
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Application",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(notificationChannel);


            FutureTarget<Bitmap> futureTarget = Glide.with(this).asBitmap().load(imageUri).submit();
            Bitmap bitmap;
            try {
                bitmap = futureTarget.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            User user = new UtilService().getUserFromSharedPref(this).getUser();
            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_24)
                    .setColor(getResources().getColor(R.color.primaryColorVariant))
                    .setLargeIcon(bitmap)
                    .setContentTitle(title+" "+user.getName())
                    .setContentText(body)
                    .setContentIntent(pi)
                    .setStyle(new Notification.BigPictureStyle())
                    .build();

            notificationManager.notify(1, notification);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
