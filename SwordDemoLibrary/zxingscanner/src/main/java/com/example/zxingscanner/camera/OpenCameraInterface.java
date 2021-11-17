package com.example.zxingscanner.camera;


import android.hardware.Camera;

import com.example.utilclass.LogCollector;

public class OpenCameraInterface {
    public static final int NO_REQUEST_CAMERA = -1;
    
    //打开相机
    public static OpenCamera openCamera(int cameraId) {
        //获取当前设备的相机数量，检测 cameraId 是否有效，获取要打开的相机的 cameraId
        int cameraNums = Camera.getNumberOfCameras();
        if (cameraNums == 0) {
            LogCollector.warn("could not found camera on device");
            return null;
        }
        
        if (cameraId >= cameraNums) {
            LogCollector.warn("Requested camera not exist: " + cameraId);
            return null;
        }
        
        if (cameraId == NO_REQUEST_CAMERA) {
            cameraId = 0;
            while (cameraId < cameraNums) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(cameraId, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    break;
                }
                ++cameraId;
            }
            if (cameraId == cameraNums) {
                LogCollector.warn("No camera facing Back, return camera #0");
                cameraId = 0;
            }
        }
        
        //通过 Camera.open(cameraId) 打开相机
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        Camera camera = Camera.open(cameraId);
        if(camera == null) {
            LogCollector.warn("camera open failed, return null");
            return null;
        }
        
        //返回 OpenCamera 对象
        return new OpenCamera(cameraId, camera, cameraInfo.facing, cameraInfo.orientation);
    }
}
