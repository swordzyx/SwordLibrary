package sword.camera;

import androidx.appcompat.app.AppCompatActivity;

import sword.camera.zxing.CameraManager;
import sword.camera.zxing.CaptureHandler;


/**
 * 相关依赖：
 * implementation 'com.google.zxing:core:3.5.1'
 * implementation 'com.google.zxing:android-core:3.3.0'
 */
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
