package sword.qrcode.cameraconfig;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.google.zxing.client.android.camera.CameraConfigurationUtils;

import sword.logger.SwordLog;

public class CameraConfigurationManager {
    private final Context context;
    private Point bestPreviewSize;
    private int cameraRotationNeed;
    private Point screenResolution;
    private Point cameraResolution;

    public CameraConfigurationManager(Context context) {
        this.context = context;
    }

    /**
     * 初始化相机预览界面大小
     * 1. 屏幕旋转角度
     * 2. 相机图像旋转角度
     * 3. 相机预览界面大小
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
                if (displayRotation % 90 == 0) {
                    displayRotationFromNature = (360 + displayRotation) % 360;
                } else {
                    throw new IllegalArgumentException("Bad rotation: " + displayRotation);
                }
                break;
        }
        SwordLog.debug("display rotation: " + displayRotationFromNature);

        //获取相机旋转角度，也就是图片或者相机预览图像的旋转角度。
        int cameraRotation = camera.getOrientation();
        SwordLog.debug("camera rotation: " + cameraRotation);

        if (camera.getFacing() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraRotationNeed = (cameraRotation + displayRotationFromNature) % 360;
            cameraRotationNeed = (360 - cameraRotationNeed) % 360;
        } else {
            cameraRotationNeed = (cameraRotation - displayRotationFromNature + 360) % 360;
        }

        SwordLog.debug("Clock rotation from display to camera: " + cameraRotationNeed);


        //获取预览界面的大小
        //获取相机的硬件参数
        Camera.Parameters parameters = camera.getCamera().getParameters();
        //获取屏幕的大小
        screenResolution = new Point();
        display.getSize(screenResolution);
        SwordLog.debug("screenResolution: x-" + screenResolution.x + "  y-" + screenResolution.y);

        if (cameraRotationNeed == 90 || cameraRotationNeed == 270) {
            cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, new Point(screenResolution.y, screenResolution.x));
        } else {
            cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
        }


        bestPreviewSize = cameraResolution;
        SwordLog.debug("Preview size on screen: " + bestPreviewSize);
    }

    /**
     * 设置曝光模式，以及是否开启闪光灯
     * 设置聚焦模式
     * 设置场景模式
     * 设置预览尺寸
     */
    public void setDesiredCameraParameters(Camera camera, boolean safeMode) {
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            SwordLog.warn("Device error: no camera parameters are available. Proceeding without configuration");
            return;
        }

        SwordLog.debug("Initial camera parameters: " + parameters.flatten());

        if (safeMode) {
            SwordLog.warn("In camera config safe mode -- most settings will not be honored");
        }

        //设置聚焦模式
        CameraConfigurationUtils.setFocus(parameters, true, false, safeMode);

        //设置预览尺寸
        SwordLog.debug("set best preview：" + bestPreviewSize.x + "---" + bestPreviewSize.y);
        parameters.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(cameraRotationNeed);

        Camera.Parameters afterParameters = camera.getParameters();
        Camera.Size afterSize = afterParameters.getPreviewSize();
        if (afterSize != null && (bestPreviewSize.x != afterSize.width || bestPreviewSize.y != afterSize.height)) {
            bestPreviewSize.x = afterSize.width;
            bestPreviewSize.y = afterSize.height;
        }
    }

    /**
     * 返回屏幕尺寸
     */
    public Point getScreenResolution() {
        return screenResolution;
    }

    /**
     * 返回相机预览尺寸
     */
    public Point getCameraResolution() {
        return cameraResolution;
    }
}
