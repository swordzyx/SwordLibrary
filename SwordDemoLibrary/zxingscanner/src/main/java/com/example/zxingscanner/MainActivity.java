package com.example.zxingscanner;

import android.os.Bundle;
import android.view.SurfaceHolder;

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
    private boolean hasSurface = false;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraManager = new CameraManager(getApplication());

        //二维码扫描框
        viewfinderView = (ViewFinderView) findViewById(R.id.view_finder_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        
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
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

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
}
