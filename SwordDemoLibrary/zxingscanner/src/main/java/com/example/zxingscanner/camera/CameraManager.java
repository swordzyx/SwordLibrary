package com.example.zxingscanner.camera;

import android.app.Application;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.example.utilclass.LogUtil;
import com.google.zxing.PlanarYUVLuminanceSource;

import java.io.IOException;

public class CameraManager {
    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MAX_FRAME_WIDTH = 1200;
    private static final int MAX_FRAME_HEIGHT = 675;
    
    private final CameraConfigurationManager configurationManager;
    private OpenCamera camera;
    private int cameraId = OpenCameraInterface.NO_REQUEST_CAMERA;
    private final PreviewCallback previewCallback;
    
    private Rect framingRectInPreview;
    private Rect framingRect;
    private boolean isPreviewing = false;
    private boolean initialized = false;

    public CameraManager(Application application) {
        configurationManager = new CameraConfigurationManager(application.getApplicationContext());
        previewCallback = new PreviewCallback(configurationManager);
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
            
            re.printStackTrace();
            if (paramFlattened != null) {
                parameters.unflatten(paramFlattened);
                try {
                    configurationManager.setDesiredCameraParameters(camera, true);
                } catch (RuntimeException ex2) {
                    ex2.printStackTrace();
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
            framingRect = null;
            framingRectInPreview = null;
        }
    }
    
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        //获取预览界面上的扫描区域
        Rect rect = getFramingRectInPreview();
        if (rect == null) {
            return null;
        }
        //返回 PlanarYUVLuminanceSource 对象，代表一个二维码扫描源，传入对应的数据，以及扫描区域的尺寸
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
    }

    /**
     * 获取预览界面上的矩形扫描区域的宽高
     * 1. 获取扫描区域的尺寸
     * 2. 计算矩形扫描区域的显示位置
     */
    public synchronized Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
            //获取扫描区域的尺寸
            Rect framingRect = getFramingRect();
            LogUtil.debug("framingRect.left: " + framingRect.left + "---framingRect.right: " + framingRect.right + "----framingRect.top: " + framingRect.top + "---framingRect.bottom: " + framingRect.bottom + "---framingRect.width: " + framingRect.width() + "----framingRect.height: " + framingRect.height());

            //获取屏幕和相机预览尺寸
            Rect rect = new Rect(framingRect);
            Point cameraResolution = configurationManager.getCameraResolution();
            Point screenResolution = configurationManager.getScreenResolution();
            LogUtil.debug("cameraResolution x: " + cameraResolution.x + "----y: " + cameraResolution.y);
            LogUtil.debug("screenResolution x: " + screenResolution.x + "----y: " + screenResolution.y);

            //计算矩形扫描区域的显示位置
            LogUtil.debug("rect.left: " + rect.left + "---rect.right: " + rect.right + "----rect.top: " + rect.top + "---rect.bottom: " + rect.bottom + "---rect.width: " + rect.width() + "----rect.height: " + rect.height());
            
            double scaleX, scaleY;
            if (configurationManager.getCameraRotationNeed() == 90 || configurationManager.getCameraRotationNeed() == 270) {
                scaleX =  (double) cameraResolution.y / (double) screenResolution.x;
                scaleY = (double) cameraResolution.x / (double) screenResolution.y;
                rect.top = (int) (framingRect.left * scaleX);
                rect.bottom = (int) (framingRect.right * scaleX);
                rect.left = (int) (framingRect.top * scaleY);
                rect.right = (int) (framingRect.bottom * scaleY);
            } else {
                scaleX =  (double) cameraResolution.x / (double) screenResolution.x;
                scaleY = (double) cameraResolution.y / (double) screenResolution.y;
                rect.left = (int) (framingRect.left * scaleX);
                rect.top = (int) (framingRect.top * scaleY);
                rect.right = (int) (framingRect.right * scaleX);
                rect.bottom = (int) (framingRect.bottom * scaleY);
            }
            LogUtil.debug("rect.left: " + rect.left + "---rect.right: " + rect.right + "----rect.top: " + rect.top + "---rect.bottom: " + rect.bottom + "---rect.width: " + rect.width() + "----rect.height: " + rect.height());

            framingRectInPreview = rect;
            LogUtil.debug("ratio x: " + scaleX);
            LogUtil.debug("ratio y: " + scaleY);


            LogUtil.debug("framingRectInPreview.left: " + framingRectInPreview.left + "---framingRectInPreview.right: " + framingRectInPreview.right + "----framingRectInPreview.top: " + framingRectInPreview.top + "---framingRectInPreview.bottom: " + framingRectInPreview.bottom + "---framingRectInPreview.width: " + framingRectInPreview.width() + "----framingRectInPreview.height: " + framingRectInPreview.height());
        }
        return framingRectInPreview;
    }

    /**
     * 获取扫描的矩形区域的尺寸
     */
    public synchronized Rect getFramingRect() {
        if(framingRect == null) {
            if (camera == null) {
                return null;
            }
            Point screenResolution = configurationManager.getScreenResolution();
            if (screenResolution == null) {
                return null;
            }
            
            //获取扫描区域的宽高 width: 5/8 * screenResolution.x, height: 5/8 * screenResolution.y
            int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
            //int height = findDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);
            
            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - width) / 2;
            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + width);
        }
        return framingRect;
    }
    
    private int findDesiredDimensionInRange(int resolution, int min, int max) {
        int dim = resolution / 2;
        if (dim < min) {
            return min;
        }
        return Math.min(dim, max);
    }

    public void startPreview() {
        OpenCamera tmpCamera = camera;
        if (camera != null && !isPreviewing) {
            tmpCamera.getCamera().startPreview();
            isPreviewing = true;
            //管理自动聚焦
            //autoFocusManager = new AutoFocusManager();
        }
    }

    public void stopPreview() {
        if (camera != null && isPreviewing) {
            camera.getCamera().stopPreview();
            previewCallback.setHandler(null, 0);
            isPreviewing = false;
        }
    }

    /**
     * 请求预览帧数据
     * @param handler
     * @param message
     */
    public synchronized void requestPreviewFrame(Handler handler, int message) {
        OpenCamera theCamera = camera;
        if (theCamera != null && isPreviewing) {
            previewCallback.setHandler(handler, message);
            //设置快照回调
            theCamera.getCamera().setOneShotPreviewCallback(previewCallback);
        }
    }
    
}
