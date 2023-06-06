package sword.camera.zxing;

import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.CountDownLatch;

import sword.camera.CameraContainerActivity;

public class DecodeThread extends Thread{
    public static final String BARCODE_BITMAP = "barcode_bitmap";
    public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";
    
    private DecodeHandler decodeHandler;
    private final CountDownLatch handlerInitLatch;
    private final CameraManager cameraManager;
    private final CameraContainerActivity activity;

    public DecodeThread(CameraContainerActivity activity) {
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
