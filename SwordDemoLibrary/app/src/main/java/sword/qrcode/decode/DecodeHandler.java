package sword.qrcode.decode;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.google.zxing.Result;

import sword.qrcode.capture.CameraManager;
import sword.qrcode.capture.CaptureHandler;
import sword.qrcode.ui.ScannerActivity;

public class DecodeHandler extends Handler {
    public static final int START_DECODE = 2001;
    public static final int QUIT_DECODE = 2002;
    private boolean running = true;
    private final CameraManager cameraManager;
    private final ScannerActivity activity;


    public DecodeHandler(ScannerActivity activity) {
        this.cameraManager = activity.getCameraManager();
        this.activity = activity;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (!running) {
            return;
        }
        switch (msg.what) {
            //CameraManager#requestPreviewFrame 在
            case START_DECODE:
                //开始识别二维码
                decode((byte[]) msg.obj, msg.arg1, msg.arg2);
                break;
            case QUIT_DECODE:
                running = false;
                Looper.myLooper().quit();
                break;
            default:
                break;
        }
    }

    private void decode(byte[] data, int width, int height) {
        if (cameraManager == null || activity == null) {
            return;
        }

        Result rawResult = CodeDecoder.decode(data, cameraManager.getFramingRectInPreview(), width, height);

        //通知主线程扫描结果
        Handler handler = activity.getHandler();
        if (handler != null) {
            Message message;
            if (rawResult != null) {
                message = Message.obtain(handler, CaptureHandler.DECODE_SUCCEED, rawResult);
            } else {
                message = Message.obtain(handler, CaptureHandler.DECODE_FAILED);
            }
            message.sendToTarget();
        }
    }
}
