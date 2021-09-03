package com.example.zxingscanner.camera;

import android.app.Application;
import android.view.SurfaceHolder;

import java.io.IOException;

public class CameraManager {
    private final CameraConfigurationManager configurationManager;
    private OpenCamera camera;
    private int cameraId = OpenCameraInterface.NO_REQUEST_CAMERA;
    private boolean initialized = false;

    public CameraManager(Application application) {
        configurationManager = new CameraConfigurationManager(application.getApplicationContext());
    }

    public void openDriver(SurfaceHolder holder) throws IOException {
        //打开相机
        OpenCamera openCamera = camera;
        if (openCamera == null) {
            openCamera = OpenCameraInterface.openCamera(cameraId);
            if (openCamera == null) {
                throw new IOException("Camera open() failed, return null to driver");
            }
            camera = openCamera;
        }


        //初始化相机参数，保存原始相机参数
        if (!initialized) {
            initialized = true;
            configurationManager.initFromCameraParameters(openCamera);
        }

        //设置需要的相机参数
        configurationManager.setDesiredCameraParameters(openCamera, false);

        //开启预览

    }
}
