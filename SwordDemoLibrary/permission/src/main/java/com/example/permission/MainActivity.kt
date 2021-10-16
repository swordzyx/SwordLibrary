package com.example.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /**
        val requestPermissionLauncher = registerForActivityResult(RequestPermission(), object: ActivityResultCallback<Boolean> {
        override fun onActivityResult(isGranted: Boolean) {
        if(isGranted) {
        //todo：用户授予权限
        } else {
        //todo：用户拒绝权限
        }
        }
        })

        以上代码可以简化成下面的代码
         */
        val requestPermissionLauncher =
            registerForActivityResult(RequestPermission()) { isGranted ->
                if (isGranted) {
                    //todo：用户授予权限
                } else {
                    //todo：用户拒绝权限
                }
            }

        when {
            //检查应用是否拥有相机权限
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                //todo: action about camera 
            }
            
            //判断是否需要显示权限说明框
            //Activity.shouldShowRequestPermissionRationale(String permission) 也可以实现同样的效果，不过这个 API 是 Android M（API 23）才引入的 
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                //todo：showInContextUI(...) ：显示权限说明 UI，向用户描述为什么要申请权限
            }
            
            //请求权限
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}