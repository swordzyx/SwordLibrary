package com.example.zxingscanner.camera;

import android.os.Handler;
import android.os.Looper;

import com.example.zxingscanner.MainActivity;

import java.util.concurrent.CountDownLatch;

public class DecodeThread extends Thread{
    public static final String BARCODE_BITMAP = "barcode_bitmap";
    public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";
    
    private DecodeHandler decodeHandler;
    private final CountDownLatch handlerInitLatch;
    private final CameraManager cameraManager;
    private final MainActivity activity;

    public DecodeThread(MainActivity activity) {
        handlerInitLatch = new CountDownLatch(1);
        this.activity = activity;
        this.cameraManager = activity.getCameraManager();
    }

    public Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return decodeHandler;
    }

    @Override
    public void run() {
        Looper.prepare();
        decodeHandler = new DecodeHandler(activity);
        handlerInitLatch.countDown();
        Looper.loop();
    }
}
