package sword.qrcode.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.swordlibrary.R;
import com.google.zxing.Result;

import sword.ToastUtilKt;
import sword.ViewUtilKt;
import sword.logger.SwordLog;
import sword.qrcode.api.ScanResultCallback;
import sword.qrcode.capture.CameraManager;
import sword.qrcode.capture.CaptureHandler;

public class ScannerActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 7700;

    private static ScanResultCallback callback;

    private CameraManager cameraManager;
    private ViewFinderView viewfinderView;
    private CaptureHandler handler;
    private Result lastResult;

    private SurfaceHolder surfaceHolder;
    private boolean hasSurface = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            ToastUtilKt.toast(this, getResources().getString(R.string.scancode_camera_not_support));
            finish();
        }

        //设置屏幕常亮
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scanner);

        SurfaceView surfaceView = findViewById(R.id.preview_view);
        surfaceView.setVisibility(View.VISIBLE);
        surfaceHolder = surfaceView.getHolder();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setRequestedOrientation(getCurrentOrientation());

        if (cameraManager == null) {
            //宽高的测量需要在 Activity 显示了之后在进行。因此 CameraManager 在此处初始化
            cameraManager = new CameraManager(getApplication());
            viewfinderView = findViewById(R.id.view_finder_view);
            viewfinderView.setCameraManager(cameraManager);
            viewfinderView.setVisibility(View.VISIBLE);
        }

        //onResume 会在 surfaceCreated 之前调用，因为执行了 addCallback 之后，才会触发 surfaceCreated 的执行。
        SwordLog.debug("hasSurface: " + hasSurface);

        //系统弹出权限申请框，用户选择允许之后，会再次回到 onResume()，但此时执行 surfaceHolder.addCallback() 之后不会立刻回调 surfaceCreated() 函数，因为此时 surfaceView 已经初始化成功了，因此 surfaceCreated() 不会被触发。
        // 将 surfaceHolder.addCallback() 放到 onCreate() 中则可以成功回调 surfaceCreated() 方法。
        //因此初次启用扫码模块并申请权限时，系统会弹出权限申请框，此时生命周期回调是：onResume -> onPause -> 系统权限申请框 -> onResume，整个流程中不会有 surfaceCreated() 和 surfaceDestroyed() 的回调。因此当用户成功授予权限之后，需要初始化相机，并开启预览。
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            if (hasSurface) {
                initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSync();
            handler = null;
        }
        //关闭相机，释放相机资源
        if (cameraManager != null) {
            cameraManager.closeDriver();
        }
        if (!hasSurface) {
            surfaceHolder.removeCallback(this);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
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
            handler.sendEmptyMessageDelayed(CaptureHandler.RESTART_PREVIEW, delayMs);
        }
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public CaptureHandler getHandler() {
        return handler;
    }

    public static void setScanResultCallback(ScanResultCallback callback) {
        ScannerActivity.callback = callback;
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
        } catch (Exception e) {
            e.printStackTrace();
            callback.scanFinish(null);
            this.finish();
        }
    }

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

    /**
     * 绘制矩形扫描区
     */
    public void drawViewFinder() {
        viewfinderView.drawViewFinder();
    }

    public void handleDecode(Result result) {
        lastResult = result;
        String rawResultString = String.valueOf(result);
        callback.scanFinish(rawResultString);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                initCamera(surfaceHolder);
            } else {
                ToastUtilKt.toast(this, getResources().getString(R.string.scancode_camera_unauthorized));
                finish();
            }
        }
    }
}
