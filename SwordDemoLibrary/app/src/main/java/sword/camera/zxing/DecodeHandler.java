package sword.camera.zxing;


import static sword.camera.zxing.DecodeThread.BARCODE_BITMAP;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.swordlibrary.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;

import sword.camera.CameraContainerActivity;

public class DecodeHandler extends Handler {
    public static final String ORIGINAL_SOURCE_PIC_KEY = "original_source_pic";
    private final MultiFormatReader formatReader;
    private boolean running = true;
    private final CameraManager cameraManager;
    private final CameraContainerActivity activity;

    public DecodeHandler(CameraContainerActivity activity) {
        formatReader = new MultiFormatReader();
        this.cameraManager = activity.getCameraManager();
        this.activity = activity;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (msg == null || !running) {
            return;
        }
        switch (msg.what) {
            //CameraManager#requestPreviewFrame 在 
            case R.id.decode:
                //开始识别二维码
                decode((byte[]) msg.obj, msg.arg1, msg.arg2);
                break;
            case R.id.quit:
                running = false;
                Looper.myLooper().quit();
                break;
            default:
                break;
        }
    }

    private void decode(byte[] data, int width, int height) {
        Result rawResult = null;
        //获取 PlanarYUVLuminanceSource 对象，表示待识别的二维码图像数据。
        PlanarYUVLuminanceSource source = cameraManager.buildLuminanceSource(data, width, height);
        //LogUtil.debug("start decode data.length = " + data.length);
        if (source != null) { 
            //LogUtil.debug("decode qrcode");
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer((source)));
            try {
                //通过 MultiFormatReader#decodeWithState 解析二维码
                rawResult = formatReader.decodeWithState(bitmap);
                //LogUtil.debug("rawResult: " + rawResult.getText());
            } catch (ReaderException re) {
                re.printStackTrace();
            } finally {
                formatReader.reset();
            }
        }

        //通知主线程扫描结果
        Handler handler = activity.getHandler();
        if (handler != null) {
            if (rawResult != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, rawResult);
                //将原始的二维码与识别结果一并返回。
                Bundle bundle = new Bundle();
                bundleThumbnail(source, bundle, BARCODE_BITMAP);
                bundleThumbnail(new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false), bundle, ORIGINAL_SOURCE_PIC_KEY);
                message.setData(bundle);
                message.sendToTarget();
            } else {
                Message message = Message.obtain(handler, R.id.decode_failed);
                message.sendToTarget();
            }
        }
    }
    
    private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle, String key) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();

        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray(key, out.toByteArray());

        bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width / source.getWidth());
    }
}