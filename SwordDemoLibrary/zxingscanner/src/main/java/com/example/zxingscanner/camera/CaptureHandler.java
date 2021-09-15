package com.example.zxingscanner.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.utilclass.LogUtil;
import com.example.zxingscanner.MainActivity;
import com.example.zxingscanner.R;
import com.google.zxing.Result;

import java.util.concurrent.CountDownLatch;

public class CaptureHandler extends Handler {
    private final MainActivity activity;
    private final CameraManager cameraManager;

    private final DecodeThread decodeThread ;
    private State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureHandler(MainActivity activity) {
        this.activity = activity;
        this.cameraManager = activity.getCameraManager();


        //开启一个解析二维码的线程
        decodeThread = new DecodeThread(activity);
        decodeThread.start();
        state = State.SUCCESS;

        //开启相机预览，并捕捉预览帧
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case R.id.decode_succeeded:
                state = State.SUCCESS;
                Bundle bundle = msg.getData();
                Bitmap barcode = null;
                float scaleFactor = 1.0f;
                if (bundle != null) {
                    byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
                    if(compressedBitmap != null) {
                        barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                        barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
                    }
                    scaleFactor = bundle.getFloat(DecodeThread.BARCODE_SCALED_FACTOR);
                }
                activity.handleDecode((Result) msg.obj, barcode, scaleFactor);
                break;
            case R.id.decode_failed:
                state = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
                break;
            case R.id.restart_preview:
                restartPreviewAndDecode();
                break;
            default:
                break;
        }
    }
    
    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            LogUtil.debug("restartPreviewAndDecode");
            state = State.PREVIEW;
            //请求预览帧
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            //绘制要扫描的矩形区域
            activity.drawViewFinder();
        }
    }
    
    public void quitSync() {
        state = State.DONE;
        cameraManager.stopPreview();
        
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();

        try {
            decodeThread.join();
        } catch (InterruptedException ignored) {
        }
        
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }
}