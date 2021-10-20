package com.example.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.utilclass.LogUtil

class MainActivity : AppCompatActivity() {
    private val permissionTest = Manifest.permission.CAMERA
    private val permission_code = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        permissionCodeManagedBySystem(permissionTest)
    }

    private fun permissionCodeManagedBySystem(permission: String) {
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
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    /**
     * 请求权限，须重写 Activity#onRequestPermissionResult(request: Int, permissions: Array<out String>, grantResults: IntArray) 函数
     */
    private fun permissionCodeManagedByCoder(
        activity: Activity,
        permission: String,
        requestCode: Int
    ) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                //app have the Permission, do action related the permission
                //todo: performAction(...)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                //todo: showInContextUI(...)
            }
            else -> {
                //请求权限
                ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
            }
        }
    }

    /**
     * 申请单个权限
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permission_code -> {
                when(grantResults.size) {
                    1 -> {
                        //申请单个权限的情况下，仅判断 grantResults（结果数组）中的第一个是否为 PackageManager.PERMISSION_GRANTED 即可
                        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            LogUtil.debug("单个权限 ${permissions[0]} 申请成功")
                        } else {
                            //用户拒绝权限
                            //向用户说明拒绝权限将无法使用特定的功能，然后让该功能不可用。
                            LogUtil.debug("单个权限 ${permissions[0]} 申请失败")
                        }
                    }
                    else -> {
                        //一次申请多个权限时，则需要遍历 permissions（所有请求的权限），挨个判断是否有对应的权限
                        for((index, per) in permissions.withIndex()) {
                            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                                LogUtil.debug("权限 $per 申请成功")
                            } else {
                                LogUtil.debug("权限 $per 申请失败")
                            }
                        }
                    }
                }
            }
        }
    }
}