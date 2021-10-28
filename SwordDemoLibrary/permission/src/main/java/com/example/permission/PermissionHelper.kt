package com.example.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

fun permissionCodeManagedBySystem(activity: AppCompatActivity, permission: String) {
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
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                //todo：用户授予权限
            } else {
                //todo：用户拒绝权限
            }
        }

    when {
        //检查应用是否拥有相机权限
        ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED -> {
            //todo: action about camera 
        }

        //判断是否需要显示权限说明框
        //Activity.shouldShowRequestPermissionRationale(String permission) 也可以实现同样的效果，不过这个 API 是 Android M（API 23）才引入的 
        /*ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            permission
        ) -> {
            //todo：showInContextUI(...) ：显示权限说明 UI，向用户描述为什么要申请权限
        }*/

        //请求权限
        else -> {
            requestPermissionLauncher.launch(permission)
        }
    }
}

fun getPrivacyIndicatorBounds(v: View) {
    v.setOnApplyWindowInsetsListener { view, insets -> 
        val indicators = insets.privacyIndicatorBounds
        insets
    }
}