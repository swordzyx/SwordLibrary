package com.example.zxingscanner;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zxingscanner.camera.CameraManager;
import com.example.zxingscanner.camera.CaptureHandler;

public abstract class ScannerActivity extends AppCompatActivity { 
    private CameraManager cameraManager;
    private CaptureHandler handler;
    
     

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication());
    }

    public CameraManager getCameraManager() {
        return this.cameraManager;
    }
    
    public CaptureHandler getHandler() {
        return handler;
    }
}
