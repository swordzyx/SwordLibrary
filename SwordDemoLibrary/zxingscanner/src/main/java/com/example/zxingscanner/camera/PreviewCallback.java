package com.example.zxingscanner.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

public class PreviewCallback implements Camera.PreviewCallback{
    private final CameraConfigurationManager configurationManager;
    private Handler previewHandler;
    private int previewMessage;
    
    public PreviewCallback(CameraConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }
    
    public void setHandler(Handler previewHandler, int message) {
        this.previewHandler = previewHandler;
        this.previewMessage = message;
    }
    
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Point cameraResolution = configurationManager.getCameraResolution();
        Handler thePreviewHandler = previewHandler;
        if (cameraResolution != null && thePreviewHandler != null) {
            Message message = thePreviewHandler.obtainMessage(previewMessage, cameraResolution.x, cameraResolution.y, data);
            message.sendToTarget();
            previewHandler = null;
        }
    }
}
