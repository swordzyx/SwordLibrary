package com.example.zxingscanner.camera;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

public class DecodeThread extends Thread{
    private DecodeHandler decodeHandler;
    private final CountDownLatch handlerInitLatch;

    public DecodeThread() {
        handlerInitLatch = new CountDownLatch(1);
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
        decodeHandler = new DecodeHandler();
        handlerInitLatch.countDown();
        Looper.loop();
    }
}
