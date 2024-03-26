package sword.qrcode.capture;

import android.app.Application;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.io.IOException;

import sword.logger.SwordLog;
import sword.qrcode.cameraconfig.CameraConfigurationManager;
import sword.qrcode.cameraconfig.OpenCamera;
import sword.qrcode.cameraconfig.OpenCameraInterface;

public class CameraManager {
    /**
     * 扫描框尺寸范围
     */
    private static final int MIN_FRAME_SIZE = 240;
    private static final int MAX_FRAME_SIZE = 1200;

    private final CameraConfigurationManager configurationManager;
    private OpenCamera camera;
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
            int cameraId = OpenCameraInterface.NO_REQUEST_CAMERA;
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
            SwordLog.warn("Camera reject parameters. Setting only minimal safe-mode parameters");
            SwordLog.warn("Resetting to saved camera params: " + paramFlattened);

            re.printStackTrace();
            if (paramFlattened != null) {
                parameters.unflatten(paramFlattened);
                try {
                    configurationManager.setDesiredCameraParameters(camera, true);
                } catch (RuntimeException ex2) {
                    ex2.printStackTrace();
                    SwordLog.warn("Camera reject even safe-mode parameters");
                }
            }
        }

        //设置预览界面
        openCamera.getCamera().setPreviewDisplay(holder);
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

    /**
     * 获取预览界面上的矩形扫描区域的宽高
     * 1. 获取扫描区域的尺寸
     * 2. 计算矩形扫描区域的显示位置
     */
    public synchronized Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
            //获取扫描区域的尺寸
            Rect framingRect = getFramingRect();
            if (framingRect == null) {
                return null;
            }

            //获取屏幕和相机预览尺寸
            Rect rect = new Rect(framingRect);
            Point cameraResolution = configurationManager.getCameraResolution();

            //计算矩形扫描区域的显示位置，当摄像头旋转了 90 度或者 270 而屏幕未旋转时，摄像头捕获的图片相对于预览时的图像也会旋转 90 度或 270 度，矩形扫描框在图像中的实际位置也会旋转对应的角度。
            rect.top = (cameraResolution.y - framingRect.height()) / 2;
            rect.bottom = rect.top + framingRect.height();
            rect.left = (cameraResolution.x - framingRect.width()) / 2;
            rect.right = rect.left + framingRect.width();

            framingRectInPreview = rect;
            SwordLog.debug("framingRectInPreview.left: " + framingRectInPreview.left + "---framingRectInPreview.right: " + framingRectInPreview.right + "----framingRectInPreview.top: " + framingRectInPreview.top + "---framingRectInPreview.bottom: " + framingRectInPreview.bottom + "---framingRectInPreview.width: " + framingRectInPreview.width() + "----framingRectInPreview.height: " + framingRectInPreview.height());
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
            int width = findDesiredDimensionInRange(Math.min(screenResolution.x, screenResolution.y));

            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - width) / 2;
            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + width);
        }
        return framingRect;
    }

    private int findDesiredDimensionInRange(int resolution) {
        int dim = resolution / 2;
        if (dim < MIN_FRAME_SIZE) {
            return MIN_FRAME_SIZE;
        }
        return Math.min(dim, MAX_FRAME_SIZE);
    }

    public void startPreview() {
        OpenCamera tmpCamera = camera;
        if (camera != null && !isPreviewing) {
            tmpCamera.getCamera().startPreview();
            isPreviewing = true;
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
