package com.example.zxingscanner.camera;


import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.zxingscanner.R;
import com.google.zxing.MultiFormatReader;

public class DecodeHandler extends Handler {
    private final MultiFormatReader formatOneDReader;
    private boolean running = true;

    public DecodeHandler() {
        formatOneDReader = new MultiFormatReader();
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (msg == null || !running) {
            return;
        }
        switch (msg.what) {
            case R.id.decode:
                decode((byte[]) msg.obj, msg.arg1, msg.arg2);
                break;
                case R.id.qui
        }
    }
}
