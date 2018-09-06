package com.example.acer.attandance_free_feature.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.acer.attandance_free_feature.MainActivity;
import com.example.acer.attandance_free_feature.R;

import java.util.Locale;

public class CountDownService extends Service {

    private static final String CHANNEL_1_ID = "channel 1";
    private static final String CHANNEL_2_ID = "channel 2";
    private static final String title = "ATTENDANCE";
    private static final long START_MILLIS_SEC = 60000;
    CountDownTimer countDownTimer;
    private long mMillisLeft;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTimer();

        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        stopSelf();
        countDownTimer.cancel();
        super.onDestroy();
    }


    private void startTimer() {

        countDownTimer = new CountDownTimer(START_MILLIS_SEC, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mMillisLeft = millisUntilFinished;

                //send data to activity
                Log.d("TEST", "send  : " +mMillisLeft);

                Intent intent = new Intent();
                intent.setAction("COUNT_DOWN");
                intent.putExtra("millistLeft", mMillisLeft);
                sendBroadcast(intent);
                notification(mMillisLeft);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent();
                intent.setAction("COUNT_DOWN");
                intent.putExtra("millisLeft", 0L);
                sendBroadcast(intent);
                notification(0);
            }
        };

        countDownTimer.start();
    }

    private void notification(long mMillisLeft) {
        final String textContent;

        if(mMillisLeft != 0){
            textContent = updateCountDownText(mMillisLeft);
        } else {
            textContent = "Done!! Ready to Check Out";
        }


        Intent intentCountDown = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentCountDown, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(title)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }


    // function

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel2.setDescription("this is channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }

    private String updateCountDownText(long mMillisLeft) {

        int hour = (int) (mMillisLeft / 1000) / 60 / 60;
        int minutes = (int) (mMillisLeft / 1000) / 60 % 60;
        int seconds = (int) (mMillisLeft / 1000) % 60;

        String settTextView = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minutes, seconds);

        return settTextView;

    }
}
