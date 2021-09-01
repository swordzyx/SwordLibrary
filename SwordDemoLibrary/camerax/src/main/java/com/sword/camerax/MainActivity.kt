package com.sword.camerax

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.utilclass.PermissionRequestUtil
import com.sword.camerax.cameraxlib.CameraXStarter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var cameraXStarter: CameraXStarter
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //init instance
        cameraXStarter = CameraXStarter()
        cameraExecutor = Executors.newSingleThreadExecutor()
        
        //检查相机权限，请求相机权限，开启预览
        if(!PermissionRequestUtil.isPermissionGranted(this, NEEDED_PERMISSION)) {
            PermissionRequestUtil.requestSpecialPermission(this, NEEDED_PERMISSION)
        } else {
            cameraXStarter.startCamera()
        }
        
        camera_capture_button.setOnClickListener {
            cameraXStarter.takePhoto()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //close Thread pool
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if(requestCode == PermissionRequestUtil.PERMISSION_RESULT_CODE) {
            if (PermissionRequestUtil.isPermissionGranted(this, NEEDED_PERMISSION)) {
                cameraXStarter.startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    
    companion object {
        const val NEEDED_PERMISSION = Manifest.permission.CAMERA 
    }
}