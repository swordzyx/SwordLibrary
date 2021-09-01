package com.example.utilclass

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PermissionRequestUtil {
    var activity: Activity? = null
    
    constructor()

    constructor(activity: Activity) {
        this.activity = activity 
    }

    val runtimePermission: Unit
        get() {
            val neededPermission: MutableList<String?> = ArrayList()
            for (permission in packagePermissionsInfo) {
                if (!isPermissionGranted(activity!!, permission)) {
                    neededPermission.add(permission)
                }
            }
            if (neededPermission.isNotEmpty()) {
                ActivityCompat.requestPermissions(activity!!, neededPermission.toTypedArray(), PERMISSION_RESULT_CODE)
            }
        }
    
    private val packagePermissionsInfo: Array<String?>
        get() = 
            try {
                //获取应用包中权限相关的信息
                val info = activity!!.packageManager.getPackageInfo(activity!!.packageName, PackageManager.GET_PERMISSIONS)
                //获取所有再清单文件中通过 <uses-permission> 声明的权限，这个列表中包含了所有已经申请的权限，包括系统授予的或未授予的权限。
                val ps = info.requestedPermissions
                if (ps != null && !ps.isNotEmpty()) {
                    ps
                } else {
                    arrayOfNulls(0)
                }
            } catch (e: Exception) {
                arrayOfNulls(0)
            }
    
    companion object {
        val PERMISSION_RESULT_CODE = 1

        fun isPermissionGranted(context: Context, permission: String?): Boolean {
            return ContextCompat.checkSelfPermission(context, permission!!) == PackageManager.PERMISSION_GRANTED
        }
        
        fun requestSpecialPermission(activity: Activity, permission: String) {
            val per = arrayOf(permission)
            
            ActivityCompat.requestPermissions(activity, per, PERMISSION_RESULT_CODE)
        }
    }
}
