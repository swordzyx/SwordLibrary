package sword.qrcode.decode;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

import sword.qrcode.ui.ScannerActivity;

public class DecodeThread extends Thread {
    private DecodeHandler decodeHandler;
    private final CountDownLatch handlerInitLatch;
    private final ScannerActivity activity;

    public DecodeThread(ScannerActivity activity) {
        handlerInitLatch = new CountDownLatch(1);
        this.activity = activity;
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
