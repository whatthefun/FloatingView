package com.example.user.floatingview;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by USER on 2016/05/25.
 */
public class SlideService extends Service {

    private WindowManager windowManager;
    private ImageView imgFloatingView;
    private WindowManager.LayoutParams params = null;
    private boolean isFloatingViewAttached = false;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Service", "onBind");
        return null;
    }

    @Override
    public void onCreate(){
        Log.i("Service", "onCreate");
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        imgFloatingView = new ImageView(this);
        imgFloatingView.setImageResource(R.drawable.ic_radio_button);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
//        frameLayout = new FrameLayout(getApplicationContext());//not sure the context is this?
        windowManager.addView(imgFloatingView, params);
//        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        layoutInflater.inflate(R.drawable.ic_radio_button, frameLayout);

        imgFloatingView.setOnTouchListener(imgFloatingViewListener);

        isFloatingViewAttached = true;
    }

    @Override
    public void onDestroy(){
        Log.i("Service", "onDestroy");
        super.onDestroy();
        removeView();
    }

    public void removeView(){
        if(imgFloatingView != null){
            windowManager.removeView(imgFloatingView);
            isFloatingViewAttached = false;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service", "onStartCommand");
        Log.i("onStartCommand", String.valueOf(isFloatingViewAttached));
        if(!isFloatingViewAttached){
            windowManager.addView(imgFloatingView, imgFloatingView.getLayoutParams());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public ImageView.OnTouchListener imgFloatingViewListener = new ImageView.OnTouchListener() {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
                case MotionEvent.ACTION_MOVE:
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);
                    windowManager.updateViewLayout(imgFloatingView, params);
                    return true;
            }
            return false;
        }

    };

}
