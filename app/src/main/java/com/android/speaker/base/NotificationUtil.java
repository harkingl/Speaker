package com.android.speaker.base;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Html;

import androidx.core.app.NotificationCompat;

import com.android.speaker.MainApplication;
import com.chinsion.SpeakEnglish.R;

public class NotificationUtil {
    private static final int NOTIFY_ID = 1;

    private static final String CHANNEL_ID = "speaker";
    private static NotificationUtil instance;
    private NotificationUtil() {

    }

    public static NotificationUtil getInstance(){
        if(instance == null){
            instance = new NotificationUtil();
        }
        return instance;
    }

    public void showNotify(int notifyId, String content, Intent intent) {
        NotificationManager manager = (NotificationManager) MainApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "开口说";
            String description = "Open Speaker";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            manager.createNotificationChannel(channel);
        }
        //用intent表现出我们要启动Notification的意图
        PendingIntent pi=PendingIntent.getActivity(MainApplication.getInstance(),0,intent,0);
        Notification notification = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(MainApplication.getInstance(), CHANNEL_ID)
                    .setContentTitle("开口说")
                    .setContentText(Html.fromHtml(content))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(MainApplication.getInstance().getResources(),R.drawable.ic_launcher))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    //默认选项，根据手机当前的环境来决定通知发出时播放的铃声，震动，以及闪光灯
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    //设置通知的权重
                    .build();
        } else {
            notification = new NotificationCompat.Builder(MainApplication.getInstance())
                    .setContentTitle("开口说")
                    .setContentText(Html.fromHtml(content))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    //默认选项，根据手机当前的环境来决定通知发出时播放的铃声，震动，以及闪光灯
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    //设置通知的权重
                    .build();
        }
        manager.notify(notifyId, notification);
    }

    public void clearNotify(int notifyId) {
        NotificationManager manager = (NotificationManager) MainApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(notifyId);
    }
}
