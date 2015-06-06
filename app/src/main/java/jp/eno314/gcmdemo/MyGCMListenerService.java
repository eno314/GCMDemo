package jp.eno314.gcmdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * GCMからのメッセージを受け取るためのサービス
 * <p/>
 * Created by eno314 on 2015/06/07.
 */
public class MyGCMListenerService extends GcmListenerService {

    private static final String TAG = RegistrationIntentService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        final String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        // TODO 受け取ったメッセージで何か処理をする
        // 一例でNotificationにメーッセージを表示させる
        sendNotification(message);
    }

    private void sendNotification(String message) {

        // Notificationクリック時にMainActivityを起動するための設定
        final Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */,
                intent, PendingIntent.FLAG_ONE_SHOT);

        // 通知音の設定
        final Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                // .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        final NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
