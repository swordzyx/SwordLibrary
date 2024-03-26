package sword.qrcode.capture;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.google.zxing.Result;


import sword.logger.SwordLog;
import sword.qrcode.decode.DecodeHandler;
import sword.qrcode.decode.DecodeThread;
import sword.qrcode.ui.ScannerActivity;

public class CaptureHandler extends Handler {
    public static final int DECODE_SUCCEED = 1001;
    public static final int DECODE_FAILED = 1002;
    public static final int RESTART_PREVIEW = 1003;

    private final ScannerActivity activity;
    private final CameraManager cameraManager;

    private final DecodeThread decodeThread ;
    private State state;

    private enum State {
        /**
         * 预览状态
         */
        PREVIEW,
        /**
         * 二维码识别成功
         */
        SUCCESS,
        /**
         * 识别结束
         */
        DONE
    }

    public CaptureHandler(ScannerActivity activity) {
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
            case DECODE_SUCCEED:
                state = State.SUCCESS;
                activity.handleDecode((Result) msg.obj);
                break;
            case DECODE_FAILED:
                state = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(), DecodeHandler.START_DECODE);
                break;
            case RESTART_PREVIEW:
                restartPreviewAndDecode();
                break;
            default:
                break;
        }
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            SwordLog.debug("restartPreviewAndDecode");
            state = State.PREVIEW;
            //请求预览帧
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), DecodeHandler.START_DECODE);
            //绘制要扫描的矩形区域
            activity.drawViewFinder();
        }
    }

    public void quitSync() {
        state = State.DONE;
        cameraManager.stopPreview();

        Message quit = Message.obtain(decodeThread.getHandler(), DecodeHandler.QUIT_DECODE);
        quit.sendToTarget();

        try {
            decodeThread.join();
        } catch (InterruptedException ignored) {
        }

        removeMessages(CaptureHandler.DECODE_SUCCEED);
        removeMessages(CaptureHandler.DECODE_FAILED);
    }
}
