package sword.qrcode.api;

import android.content.Context;
import android.content.Intent;

import sword.qrcode.ui.PhotoActivity;
import sword.qrcode.ui.ScannerActivity;

public class CodeScanner {
    public static void scan(Context context, ScanResultCallback callback) {
        if (callback == null) {
            return;
        }

        ScannerActivity.setScanResultCallback(callback);
        context.startActivity(new Intent(context, ScannerActivity.class));
    }

    public static void scanPicture(Context context, ScanResultCallback callback) {
        if (callback == null) {
            return;
        }

        PhotoActivity.setCallback(callback);
        context.startActivity(new Intent(context, PhotoActivity.class));
    }
}
