package com.swordlibrary

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import kotlin.system.exitProcess

object AppUtils {
    private const val TAG = "AppUtils"

    fun isAppRunning(packageName: String): Boolean {
        if (packageName.isEmpty()) {
            LogUtils.e("package name is empty")
            return false
        }

        val am = App.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        //获取正在运行 app 的任务栈   getRunningTask 于 API 21 废弃 Android 5.0
        am.getRunningTasks(Int.MAX_VALUE).forEach { runningTaskInfo ->
            LogUtils.d(TAG, "app name: ${runningTaskInfo?.baseActivity?.className}")
            if (packageName == runningTaskInfo?.baseActivity?.packageName) {
                return true
            }
        }

        //获取正在运行的服务  getRunningServices 于 API 21 废弃 Android 5.0
        am.getRunningServices(Int.MAX_VALUE).forEach { runningServiceInfo ->
            LogUtils.d(TAG, "running service name: ${runningServiceInfo?.service?.className}")
            if (packageName == runningServiceInfo?.service?.packageName) {
                return true
            }
        }
        return false
    }

    fun relaunchApp(context: Context) {
        context.run {
            getRestartIntent(context)?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
            }
            exitProcess(0)
        }
    }

    /**
     * 参考
     */
    private fun getRestartIntent(context: Context): Intent? {
        val packageManager = context.packageManager
        val packageName = context.packageName

        var intent: Intent? = null
        //Use leanback intent if available, for Android TV apps.
        if (Build.VERSION.SDK_INT >= 21 && packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)) {
            intent = packageManager.getLeanbackLaunchIntentForPackage(packageName)
        }

        if (intent == null) {
            intent = packageManager.getLaunchIntentForPackage(packageName)
        }
        return intent
    }
}