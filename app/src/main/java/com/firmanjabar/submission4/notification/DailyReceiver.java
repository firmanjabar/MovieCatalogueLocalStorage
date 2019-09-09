package com.firmanjabar.submission4.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.firmanjabar.submission4.R;
import com.firmanjabar.submission4.feature.main.MainActivity;

import java.util.Calendar;
import java.util.Random;

public class DailyReceiver extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 100;
    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String EXTRA_TYPE = "extra_type";

    public DailyReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context, context.getResources().getString(R.string.message_daily), intent.getStringExtra(EXTRA_MESSAGE), new Random().nextInt(300));
    }

    private void sendNotification(Context context, String title, String desc, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "chanel_id";
            CharSequence name = "Channel Movie Catalogue";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(mChannel);
            builder = new NotificationCompat.Builder(context, chanel_id);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntent(intent)
                .getPendingIntent(NOTIFICATION_ID,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_movie)
                .setContentTitle(title)
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(uriTone);
        if (notificationManager != null) {
            notificationManager.notify(id, builder.build());
        }

    }

    public void setAlarm(Context context, String type, String time, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,DailyReceiver.class);
        intent.putExtra(EXTRA_MESSAGE,message);
        intent.putExtra(EXTRA_TYPE,type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND,0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,NOTIFICATION_ID,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context, R.string.active_daily_reminder, Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,DailyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,NOTIFICATION_ID,intent,0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, R.string.deactive_daily_remindedr, Toast.LENGTH_SHORT).show();
    }
}
