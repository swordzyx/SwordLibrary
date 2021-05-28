package com.example.hencodersourcecode

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        drapBadge()

        getDataDir(this)
    }


    fun getDataDir(activity: Context) {
        var path = ""
        val PACKAGE_NAME = getAppInfo(activity)
        path = if (isSdCardExist()) {
            Environment.getExternalStorageDirectory().path
        } else {
            activity.filesDir.path
        }
        path += "/" + PACKAGE_NAME + "_001"
        val dir = File(path)
        Log.d("Sword", "存储目录：$path")
    }

    /**
     * 获取应用包名
     *
     * @return
     */
    fun getAppInfo(activity: Context): String? {
        try {
            val pkName = activity.packageName
            val versionName = activity.packageManager.getPackageInfo(
                pkName, 0
            ).versionName
            val versionCode = activity.packageManager.getPackageInfo(
                pkName, 0
            ).versionCode
            return pkName + "_" + versionName + "_" + versionCode
        } catch (e: Exception) {
        }
        return null
    }


    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    fun isSdCardExist(): Boolean {
        try {
            return Environment.getExternalStorageState() ==
                    Environment.MEDIA_MOUNTED
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}