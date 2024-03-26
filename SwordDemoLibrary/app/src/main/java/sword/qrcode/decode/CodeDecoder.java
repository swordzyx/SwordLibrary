package sword.qrcode.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import sword.logger.SwordLog;

public class CodeDecoder {
    public static final Map<DecodeHintType, Object> DEFAULT_HINTS = new EnumMap<>(DecodeHintType.class);
    public static final int DEFAULT_REQ_WIDTH = 480;
    public static final int DEFAULT_REQ_HEIGHT = 640;

    static {
        DEFAULT_HINTS.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
        DEFAULT_HINTS.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        DEFAULT_HINTS.put(DecodeHintType.TRY_HARDER, BarcodeFormat.QR_CODE);
        DEFAULT_HINTS.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    public static Result decode(byte[] data, Rect scanRect, int width, int height) {
        MultiFormatReader formatReader = new MultiFormatReader();
        //获取 PlanarYUVLuminanceSource 对象，表示待识别的二维码图像数据。
        formatReader.setHints(DEFAULT_HINTS);
        LuminanceSource source = buildLuminanceSource(data, scanRect, width, height);
        return decodeInternal(formatReader, source);
    }

    public static Result decodeQRCode(Bitmap bitmap) {
        QRCodeMultiReader formatReader = new QRCodeMultiReader();
        LuminanceSource source = getRGBLuminanceSource(bitmap);
        return decodeInternal(formatReader, source);
    }

    public static Result decodeQRCode(String filePath) {
        return decodeQRCode(compressBitmap(filePath));
    }

    private static Result decodeInternal(QRCodeMultiReader reader, LuminanceSource source) {
        Result[] results = null;
        try {
            try {
                // GlobalHistogramBinarizer 解析速度更快，精度更高，不过无法处理处理二维码中的阴影和渐变两个情况
                BinaryBitmap binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
                SwordLog.debug("width: " + binaryBitmap.getWidth() + ", height: " + binaryBitmap.getHeight() /*+ ", BlackMatrix: " + binaryBitmap*/);
                results = reader.decodeMultiple(binaryBitmap, DEFAULT_HINTS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (results == null || results.length == 0) {
            return null;
        }
        return results[0];
    }

    private static Result decodeInternal(MultiFormatReader reader, LuminanceSource source) {
        Result result = null;
        try {
            try {
                //采用HybridBinarizer解析
                result = reader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (result == null) {
                //如果没有解析成功，再采用 GlobalHistogramBinarizer 解析一次
                result = reader.decodeWithState(new BinaryBitmap(new GlobalHistogramBinarizer(source)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Bitmap compressBitmap(String filePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        float width = opts.outWidth;
        float height = opts.outHeight;

        int wSize = 1;
        if (width > DEFAULT_REQ_WIDTH) {
            wSize = (int) (width / DEFAULT_REQ_WIDTH);
        }
        int hSize = 1;
        if (height > DEFAULT_REQ_HEIGHT) {
            hSize = (int) (height / DEFAULT_REQ_HEIGHT);
        }
        int size = Math.max(wSize, hSize);
        if (size <= 0)
            size = 1;

        opts.inSampleSize = size;// 设置缩放比例
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, opts);
    }

    /**
     * 获取RGBLuminanceSource
     */
    static RGBLuminanceSource getRGBLuminanceSource(@NonNull Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return new RGBLuminanceSource(width, height, pixels);
    }

    static PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, Rect scanRect, int width, int height) {
        if (scanRect == null) {
            return null;
        }
        //返回 PlanarYUVLuminanceSource 对象，代表一个二维码扫描源，传入对应的数据，以及扫描区域的尺寸
        return new PlanarYUVLuminanceSource(data, width, height, scanRect.left, scanRect.top, scanRect.width(), scanRect.height(), false);
    }
}
