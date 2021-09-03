package com.example.zxingscanner.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.example.utilclass.LogUtil;
import com.google.zxing.client.android.camera.CameraConfigurationUtils;

public class CameraConfigurationManager {
    private final Context context;
    private Point bestPreviewSize;
    private int cameraRotationNeed;

    public CameraConfigurationManager(Context context) {
        this.context = context;
    }

    /**
     * 初始化相机相关参数
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
    
    
    public void setDesiredCameraParameters(OpenCamera camera, boolean saveMode) {
        
    }


}
