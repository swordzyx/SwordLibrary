package com.sword

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import com.example.utilclass.dongliu.apk.parser.xlcw.configParam
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar

private var toast: Toast? = null
private var snake: Snackbar? = null

fun toast(context: Context, msg: String, duration: Int = Toast.LENGTH_SHORT, callback: Toast.Callback? = null) {
    val toast = Toast.makeText(context.applicationContext, msg, Toast.LENGTH_SHORT)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && callback != null) {
        toast.addCallback(callback)
    }
    toast.show()
}

//todo：会出现 snakeBar 无法显示的情况
fun snackBar(activity: Activity, msg: String, isLong: Boolean) {
    snackBar(activity.window.decorView, msg, isLong)
}

fun snackBar(rootView: View, msg: String, isLong: Boolean) {
    val snackBar = Snackbar.make(rootView, msg, if (isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT)
    snackBar.show()
}

