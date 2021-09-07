package com.example.zxingscanner.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.example.utilclass.LogUtil;
import com.google.zxing.client.android.camera.CameraConfigurationUtils;

import java.security.Policy;

public class CameraConfigurationManager {
    private final Context context;
    private Point bestPreviewSize;
    private int cameraRotationNeed;

    public CameraConfigurationManager(Context context) {
        this.context = context;
    }

    /**
     * 初始化相机预览界面大小
     * 1. 屏幕旋转角度
     * 2. 相机图像旋转角度
     * 3. 相机预览界面大小
     * @param camera
     * @throws IllegalArgumentException
     */
    public void initFromCameraParameters(OpenCamera camera) throws IllegalArgumentException {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        //获取屏幕旋转角度。
        int displayRotation = display.getRotation();
        int displayRotationFromNature;
        switch (displayRotation) {
            case Surface.ROTATION_0:
                displayRotationFromNature = 0;
                break;
            case Surface.ROTATION_90:
                displayRotationFromNature = 90;
                break;
            case Surface.ROTATION_180:
                displayRotationFromNature = 180;
                break;
            case Surface.ROTATION_270:
                displayRotationFromNature = 270;
                break;
            default:
                if(displayRotation % 90 == 0) {
                    displayRotationFromNature = (360 + displayRotation) % 360;
                } else {
                    throw new IllegalArgumentException("Bad rotation: " + displayRotation);
                }
                break;
        }
        LogUtil.debug("display rotation: " + displayRotationFromNature);
        
        //获取相机旋转角度，也就是图片或者相机预览图像的旋转角度。
        int cameraRotation = camera.getOrientation();
        //如果设备竖屏，而相机图像显示为横屏，对于后置摄像头则是旋转了 90 度，而对于前置摄像头则是旋转了 270 度。
        if(camera.getFacing() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraRotation = (360 - cameraRotation) % 360;
        }
        LogUtil.debug("camera rotation: " + cameraRotation);
        
        int rotationFromCameraToDisplay = (360 + cameraRotation - displayRotation) % 360;
        if (camera.getFacing() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraRotationNeed = (360 - rotationFromCameraToDisplay) % 360;
        } else {
            cameraRotationNeed = rotationFromCameraToDisplay;
        }
        LogUtil.debug("Clock rotation from display to camera: " + rotationFromCameraToDisplay);


        //获取预览界面的大小
        //获取相机的硬件参数
        Camera.Parameters parameters = camera.getCamera().getParameters();
        //获取屏幕的大小
        Point screenResolution = new Point();
        display.getSize(screenResolution);
        Point cameraPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
        
        boolean isScreenPortrait = screenResolution.x < screenResolution.y;
        boolean isPreviewPortrait = bestPreviewSize.x < bestPreviewSize.y;
        
        if (isScreenPortrait == isPreviewPortrait) {
            bestPreviewSize = cameraPreviewSize;
        } else {
            bestPreviewSize = new Point(cameraPreviewSize.y, cameraPreviewSize.x);
        }
        LogUtil.debug("Preview size on screen: " + bestPreviewSize);
    }


    /**
     * 设置曝光模式，以及是否开启闪光灯
     * 设置聚焦模式
     * 设置场景模式
     * 设置预览尺寸
     * @param camera
     * @param safeMode
     */
    public void setDesiredCameraParameters(Camera camera, boolean safeMode) {
        Camera.Parameters parameters = camera.getParameters();
        if(parameters == null) {
            LogUtil.warn("Device error: no camera parameters are available. Proceeding without configuration");
            return;
        }

        LogUtil.debug("Initial camera parameters: " + parameters.flatten());

        //? 这里没有理解是什么意思
        if (safeMode) {
            LogUtil.warn("In camera config safe mode -- most settings will not be honored");
        }

        //初始化曝光补偿以及是否开启闪光灯，默认不开启曝光补偿和闪光灯
        initTorch(camera, false);

        //设置聚焦模式
        CameraConfigurationUtils.setFocus(parameters, true, false, safeMode);

        if(!safeMode) {
            //设置场景模式
            CameraConfigurationUtils.setBarcodeSceneMode(parameters);
        }

        //设置预览尺寸
        parameters.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);
        //设置参数，使参数生效
        camera.setParameters(parameters);
        camera.setDisplayOrientation(cameraRotationNeed);

        //重新设置预览尺寸，以适配设置了参数之后的推荐预览尺寸。
        Camera.Parameters afterParameters = camera.getParameters();
        Camera.Size afterSize = afterParameters.getPreviewSize();
        if (afterSize != null && (bestPreviewSize.x != afterSize.width || bestPreviewSize.y != afterSize.height)) {
            bestPreviewSize.x = afterSize.width;
            bestPreviewSize.y = afterSize.height;
        }
    }

    private void initTorch(Camera camera, boolean torch) {
        setTorch(camera.getParameters(), torch, false);
    }

    private void setTorch(Camera.Parameters parameters, boolean torch, boolean safeMode) {
        CameraConfigurationUtils.setTorch(parameters, torch);
        if (!safeMode) {
            CameraConfigurationUtils.setBestExposure(parameters, torch);
        }
    }


}
