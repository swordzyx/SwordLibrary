package com.example.zxingscanner.camera;

import android.app.Application;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.example.utilclass.LogUtil;

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


        //初始化相机参数，获取合适的预览尺寸，以及屏幕旋转角度
        if (!initialized) {
            initialized = true;
            configurationManager.initFromCameraParameters(openCamera);
        }


        Camera camera = openCamera.getCamera();
        Camera.Parameters parameters = camera.getParameters();
        String paramFlattened = parameters == null ? null : parameters.flatten();
        try {
            //设置需要的相机参数，曝光模式，场景模式，闪光灯，预览尺寸等
            configurationManager.setDesiredCameraParameters(camera, false);
        } catch (RuntimeException re) {
            LogUtil.warn("Camera reject parameters. Setting only minimal safe-mode parameters");
            LogUtil.debug("Resetting to saved camera params: " + paramFlattened);

            if (paramFlattened != null) {
                parameters.unflatten(paramFlattened);
                try {
                    configurationManager.setDesiredCameraParameters(camera, true);
                } catch (RuntimeException ex2) {
                    LogUtil.warn("Camera reject even safe-mode parameters");
                }
            }
        }

        //设置预览界面
        camera.setPreviewDisplay(holder);
    }

    public synchronized boolean isOpen() {
        return camera != null;
    }

    public void closeDriver() {
        if(camera != null) {
            camera.getCamera().release();
            camera = null;
        }
    }
}
