package sword.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.swordlibrary.R;
import com.google.zxing.Result;

import sword.PermissionUtilKt;
import sword.SwordLog;

import java.io.IOException;

import sword.camera.zxing.CameraManager;
import sword.camera.zxing.CaptureHandler;

public class CameraContainerActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private CameraManager cameraManager;
    private ViewFinderView viewfinderView;
    private boolean hasSurface = false;

    private CaptureHandler handler;
    private Result lastResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwordLog.debug("onCreate");

        //设置屏幕常亮
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera_container);

        if (!PermissionUtilKt.isPermissionGranted(this, Manifest.permission.CAMERA)) {
            PermissionUtilKt.requestSinglePermission(this, Manifest.permission.CAMERA, 0);
        }
        
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(this, "当前设备不支持相机", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SwordLog.debug("onResume");
        
        setRequestedOrientation(getCurrentOrientation());

        //宽高的测量需要在 Activity 显示了之后在进行。因此 CameraManager 在此处初始化
        cameraManager = new CameraManager(getApplication());
        viewfinderView = findViewById(R.id.view_finder_view);
        viewfinderView.setCameraManager(cameraManager);
        viewfinderView.setVisibility(View.VISIBLE);

        //onResume 会在 surfaceCreated 之前调用，因为执行了 addCallback 之后，才会触发 surfaceCreated 的执行。
        SwordLog.debug("hasSurface: " + hasSurface);

        SurfaceView surfaceView = findViewById(R.id.preview_view);
        surfaceView.setVisibility(View.VISIBLE);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if(hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        SwordLog.debug("onPause");
        if (handler != null) {
            handler.quitSync();
            handler = null;
        }
        cameraManager.closeDriver();

        //关闭相机，释放对应资源
        //surfaceDestroyed 会在 onPause 之前执行.
        if (!hasSurface) {
            SurfaceView surfaceView = findViewById(R.id.preview_view);
            SurfaceHolder holder = surfaceView.getHolder();
            holder.removeCallback(this);
        }

    }

    @Override
    protected void onDestroy() {
        SwordLog.debug("onDestroy");
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        SwordLog.debug("surfaceCreated");
        if (!hasSurface) {
            hasSurface = true;
            //初始化相机
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (lastResult != null) {
                restartPreviewAfterDelay(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void restartPreviewAfterDelay(long delayMs) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMs);
        }
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public CaptureHandler getHandler() {
        return handler;
    }

    private void initCamera(SurfaceHolder holder) {
        SwordLog.debug("initCamera");
        if (holder == null) {
            throw new IllegalStateException("No SurfaceHolder");
        }

        //相机已经处于开启状态，则直接返回
        if (cameraManager.isOpen()) {
            SwordLog.warn("camera has been open");
            return;
        }
        
        //打开相机
        try {
            cameraManager.openDriver(holder);
            if (handler == null) {
                handler = new CaptureHandler(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这个方法还不是很懂
     */
    @SuppressLint("SwitchIntDef")
    private int getCurrentOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_90:
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                default:
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
        } else {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_270:
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                default:
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
        }
    }

    //绘制矩形扫描区
    public void drawViewFinder() {
        viewfinderView.drawViewFinder();
    }
    
    public void handleDecode(Result result) {
        lastResult = result;
        String rawResultString = String.valueOf(result);
        Toast.makeText(this, rawResultString, Toast.LENGTH_SHORT).show();
        //finish();
    }

    public void setCodeImage(Bitmap barcode) {
        SwordLog.debug("bitmap.width: " + barcode.getWidth() + "---bitmap.height: " + barcode.getHeight());
        ((ImageView)findViewById(R.id.code_image)).setVisibility(View.VISIBLE);
        ((ImageView)findViewById(R.id.code_image)).setImageBitmap(barcode);
    }

    public void setOriginalCapture(Bitmap bitmap) {
        SwordLog.debug("original width: " + bitmap.getWidth() + "---original height: " + bitmap.getHeight());
        ((ImageView)findViewById(R.id.source_image)).setVisibility(View.VISIBLE);
        ((ImageView)findViewById(R.id.source_image)).setImageBitmap(bitmap);
    }
}
