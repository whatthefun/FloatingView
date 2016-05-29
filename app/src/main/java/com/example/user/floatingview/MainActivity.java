package com.example.user.floatingview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private final static int ID = 606;
    private Switch switcher;
    private NotificationManager notificationManager;
    private boolean isFloatingViewShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switcher = (Switch) findViewById(R.id.switcher);
        switcher.setOnCheckedChangeListener(switcherListener);
    }

    private Switch.OnCheckedChangeListener switcherListener = new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                showNotification(getApplicationContext());
            }else{
                notificationManager.cancel(ID);
            }
        }
    };

    private void showNotification(Context context){
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, SlideService.class);
        int flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        intent.setFlags(flags);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);


        long[] vibrateTime = {300, 400};

        Notification notification = new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_touch_app_black)
                .setContentTitle("滑頁工具")
                .setContentText("點擊開啟滑頁工具")
                .setTicker("開啟!")
                .setLights(0xFFFFFFFF, 1000, 1000)
                .setVibrate(vibrateTime)
//                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();


        notificationManager.notify(ID, notification);
    }



}

