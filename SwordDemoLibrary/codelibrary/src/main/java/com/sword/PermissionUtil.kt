package com.sword

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

interface PermissionCallback {
    /**
     * 应用获取权限
     */
    fun permissionGrant()

    /**
     * 用户拒绝授予权限
     */
    fun permissionReject()

    /**
     * 显示权限请求说明提示框，在请求权限前调用
     */
    fun showRationaleUI()

    /**
     * 应用已经获取权限
     */
    fun permissionHaveGranted()
}

/**
 * 此方法须在 onCreate 中调用，因为 registerForActivityResult 函数必须在 onCreate() 函数中调用，否则会抛异常。
 * 
 * [activity]: 需要申请权限的 Activity
 * [callback]: PermissionCallback 接口实例，权限申请相关回调接口，需要实现以下函数
 *      1. permissionGrant()：用户授予权限
 *      2. permissionReject()：用户拒绝权限
 *      3. showRationaleUI()：显示权限说明 UI，用于告知用户为什么要申请权限
 *      4. permissionHaveGranted()：应用已经拥有权限
 */
fun requestByLauncher(activity: AppCompatActivity, callback: PermissionCallback, permission: String, launcher: ActivityResultLauncher<String>? = null) {
    val l = launcher ?: activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted)  callback.permissionGrant() else callback.permissionReject()
    }
    
    when {
        ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED -> {
            callback.permissionHaveGranted()
        }
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
            callback.showRationaleUI()
        }
        else -> {
            l.launch(permission)
        }
    }
}




const val PERMISSION_RESULT_CODE = 1001

fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

/**
 * 申请单个权限。
 * [activity]：需要获取权限的 Activity
 * [permission]：需要请求的权限
 * [requestCode]：权限请求码，默认为 [PERMISSION_RESULT_CODE]
 */
fun requestSinglePermission(activity: Activity, permission: String, requestCode: Int = PERMISSION_RESULT_CODE) {
    val per = arrayOf(permission)
    ActivityCompat.requestPermissions(activity, per, requestCode)
}

/**
 * 申请多个权限，需要重写 Activity#onRequestPermissionsResult() 方法，如果没有传入 requestCode 参数，则默认权限请求码为 [PERMISSION_RESULT_CODE]，该请求码也会传到 Activity#onRequestPermissionResult() 中。
 * [activity]：需要请求权限的 Activity
 * [permissions]：权限数组，里面包含需要请求的一个或多个权限
 * [requestCode]：权限请求码，默认为 [PERMISSION_RESULT_CODE]
 */
fun requestMultiPermission(activity: Activity, permissions: Array<String>, requestCode: Int = PERMISSION_RESULT_CODE) {
    val needGrantPers = permissions.filterNot { 
        isPermissionGranted(activity, it)
    }
    
    if(needGrantPers.isNotEmpty()) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, needGrantPers[0])) {
            //todo: 显示权限用处说明 UI，告知用户为何需要使用这些权限，以及由用户决定是否继续授予权限
            //todo: 如果用户同意继续申请权限，则继续调用 requestMultiPermission(activity, needGrantPers.toTypedArray())
        } else {
            ActivityCompat.requestPermissions(activity, needGrantPers.toTypedArray(), requestCode)
        }
    }
}
