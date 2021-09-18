package com.example.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.utilclass.LogUtil;
import com.example.utilclass.PermissionRequestUtil;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    private Camera camera;
    private CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        PermissionRequestUtil.Companion.requestSpecialSinglePermission(this, Manifest.permission.CAMERA);
        
        SurfaceView previewView = (SurfaceView) findViewById(R.id.preview_view);
        previewView.getHolder().addCallback(callback);

        cameraManager = new CameraManager();
        //获取相机实例
        camera = cameraManager.getCameraInstance(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            LogUtil.debug("surfaceCreated");
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                LogUtil.error("start Preview Failed, error message: " + e.getMessage());
            }
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            LogUtil.debug("surfaceChanged");
            if (holder.getSurface() == null) {
                return;
            }
            try {
                camera.stopPreview();
            } catch (Exception e) {
                LogUtil.error("stop Preview Failed, error message: " + e.getMessage());
            }

            try {
                //开启预览
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                LogUtil.error("start preview failed, error message: " + e.getMessage());
            }
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            LogUtil.debug("surfaceDestroyed");
        }
    };
}