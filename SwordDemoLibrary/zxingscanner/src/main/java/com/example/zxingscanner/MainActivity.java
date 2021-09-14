package com.example.zxingscanner;

import android.Manifest;
import android.content.pm.ActivityInfo;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utilclass.LogUtil;
import com.example.utilclass.PermissionRequestUtil;
import com.example.zxingscanner.camera.CameraManager;
import com.example.zxingscanner.camera.CaptureHandler;
import com.google.zxing.Result;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
    private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

    private CameraManager cameraManager;
    private ViewFinderView viewfinderView;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean hasSurface = false;

    private CaptureHandler handler;
    private Result lastResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.debug("onCreate");

        //设置屏幕常亮
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        if (!PermissionRequestUtil.Companion.isPermissionGranted(this, Manifest.permission.CAMERA)) {
            PermissionRequestUtil.Companion.requestSpecialPermission(this, Manifest.permission.CAMERA);
        }

        //UI 初始化
        //二维码扫描框
        /*viewfinderView = (ViewFinderView) findViewById(R.id.view_finder_view);
        viewfinderView.setVisibility(View.VISIBLE);

        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        surfaceView.getHolder().addCallback(this);
        surfaceHolder = surfaceView.getHolder();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.debug("onResume");
        
        setRequestedOrientation(getCurrentOrientation());

        //宽高的测量需要在 Activity 显示了之后在进行。因此 CameraManager 在此处初始化
        cameraManager = new CameraManager(getApplication());
        viewfinderView = (ViewFinderView) findViewById(R.id.view_finder_view);
        viewfinderView.setCameraManager(cameraManager);
        viewfinderView.setVisibility(View.VISIBLE);

        //onResume 会在 surfaceCreated 之前调用，因为执行了 addCallback 之后，才会触发 surfaceCreated 的执行。
        LogUtil.debug("hasSurface: " + hasSurface);
        
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        surfaceView.setVisibility(View.VISIBLE);
        surfaceHolder = surfaceView.getHolder();
        if(hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        LogUtil.debug("surfaceCreated");
        if (holder == null) {
            LogUtil.error("null surface");
        }
        if (!hasSurface) {
            hasSurface = true;
            //初始化相机
            initCamera(holder);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.debug("onPause");
        if (handler != null) {
            handler.quitSync();
            handler = null;
        }
        cameraManager.closeDriver();

        //关闭相机，释放对应资源
        //surfaceDestroyed 会在 onPause 之前执行.
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder holder = surfaceView.getHolder();
            holder.removeCallback(this);
        }

    }

    @Override
    protected void onDestroy() {
        LogUtil.debug("onDestroy");
        super.onDestroy();
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
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (lastResult != null) {
                    restartPreviewAfterDelay(0);
                }
                break;
            default:
                break;
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
        LogUtil.debug("initCamera");
        if (holder == null) {
            throw new IllegalStateException("No SurfaceHolder");
        }

        //相机已经处于开启状态，则直接返回
        if (cameraManager.isOpen()) {
            LogUtil.warn("camera has been open");
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
     *
     * @return
     */
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

    public void drawViewFinder() {
        viewfinderView.drawViewFinder();
    }

    /**
     * @param result
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result result, Bitmap barcode, float scaleFactor) {
        lastResult = result;
        String rawResultString = String.valueOf(result);
        if (rawResultString.length() > 32) {
            Toast.makeText(this, rawResultString.substring(0, 32), Toast.LENGTH_SHORT).show();
        }
    }
}
