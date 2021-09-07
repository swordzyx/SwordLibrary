package com.example.zxingscanner.camera;

import android.app.Activity;
import android.graphics.Paint;

import java.util.concurrent.CountDownLatch;

public class CaptureHandler {
    private final Activity activity;
    private final CameraManager cameraManager;

    private final DecodeThread decodeThread ;
    private State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureHandler(Activity activity, CameraManager cameraManager) {
        this.activity = activity;
        this.cameraManager = cameraManager;


        //开启一个解析二维码的线程
        decodeThread = new DecodeThread();
        decodeThread.start();
        state = State.SUCCESS;

        //开启相机预览，并捕捉预览帧
    }
}
