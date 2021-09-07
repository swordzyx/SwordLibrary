package com.example.zxingscanner;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utilclass.LogUtil;
import com.example.zxingscanner.camera.CameraManager;
import com.example.zxingscanner.camera.CameraConfigurationManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private CameraManager cameraManager;
    private ViewFinderView viewfinderView;
    private boolean hasSurface;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置屏幕常亮
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        //当设备处于电池供电时，如果当前 Activity 一段时间不活动，则结束它
        //inactivityTimer = new InActivityTimer(this)
        //用于播放声音和震动
        //beepManger = new BeepManager(this)
        //检测环境光，并在环境非常暗时打开前置灯，并在足够亮时关闭
        //ambientLightManager = new AmbientLightManger(this)

        hasSurface = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //宽高的测量需要在 Activity 显示了之后在进行。因此 CameraManager 在此处初始化
        cameraManager = new CameraManager(getApplication());

        //二维码扫描框
        viewfinderView = (ViewFinderView) findViewById(R.id.view_finder_view);
        viewfinderView.setCameraManager(cameraManager);

        //设置 Activity 的方向
        setRequestedOrientation(getCurrentOrientation());

        //更新声音和震动的偏好设置
        //beepManager.updatePrefs();
        //开启环境光检测
        //ambientLightManager.start(cameraManger)
        //在 Activity 未活动时关闭
        //inactivityTimer.onResume();

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        SurfaceHolder holder = surfaceView.getHolder();
        //onResume 会在 surfaceCreated 之前调用，因为执行了 addCallback 之后，才会出发 surfaceCreated 的执行。
        if(hasSurface) {
            initCamera(holder);
        } else {
            holder.addCallback(this);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
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
        //inactivityTimer.onPause()
        //ambientLightManger.stop()
        //beepManager.close()
        cameraManager.closeDriver();

        //关闭相机，释放对应资源
        //surfaceDestroyed 会在 onPause 之前执行.
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);
            SurfaceHolder holder = surfaceView.getHolder();
            holder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //inactivityTimer.shutdown()
        super.onDestroy();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        hasSurface = false;
    }
    
    private void initCamera(SurfaceHolder holder) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这个方法还不是很懂
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
}
