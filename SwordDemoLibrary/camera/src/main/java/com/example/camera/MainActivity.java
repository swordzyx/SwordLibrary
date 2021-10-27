package com.example.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.utilclass.LogUtil;
import com.example.utilclass.PermissionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    
    private Camera camera;
    private CameraPreview preview;
    private CameraManager cameraManager;    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtil.Companion.requestSpecialSinglePermission(this, Manifest.permission.CAMERA);
        
        cameraManager = new CameraManager();
        camera = cameraManager.getCameraInstance(Camera.CameraInfo.CAMERA_FACING_FRONT);
        
        preview = new CameraPreview(this, camera);
        FrameLayout previewContainer = (FrameLayout) findViewById(R.id.camera_preview);
        previewContainer.addView(preview);
    }
    
    
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File[] mediaFiles = MainActivity.this.getExternalMediaDirs(); 
            
            for (File file : mediaFiles) {
                LogUtil.debug("getExternalMediaDirs: " + file.getAbsolutePath());
            }
            File pictureFile = new File(mediaFiles[0], "capture.jpg");

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    
    
}