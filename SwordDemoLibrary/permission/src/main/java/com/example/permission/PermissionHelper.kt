package com.example.permission

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorPrivacyManager
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

/**
 * Android 12 以后，应用访问麦克风或者相机时，会在屏幕右上角显示一个小图标，用于提示用户当前有应用在使用麦克风或者相机。通过以下方法获取小图标在屏幕上的位置。
 */
fun getPrivacyIndicatorBounds(v: View) {
    v.setOnApplyWindowInsetsListener { view, insets ->
        val indicators = if (Build.VERSION.SDK_INT == 31) insets.privacyIndicatorBounds else null
        insets
    }
}

/**
 * 检查设备是否支持麦克风和相机可用状态的切换。
 */
fun checkDeviceSupport(context: Context) {
    if (Build.VERSION.SDK_INT == 31) {
        //toggle 中文有切换键，开关的意思
        val sensorPrivacyManager = context.getSystemService(SensorPrivacyManager::class.java) as SensorPrivacyManager
        val supportsMicroPhoneToggle = sensorPrivacyManager.supportsSensorToggle(SensorPrivacyManager.Sensors.MICROPHONE)
        val supportsCameraToggle = sensorPrivacyManager.supportsSensorToggle(SensorPrivacyManager.Sensors.CAMERA)
    }
}