package com.sword

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import com.example.utilclass.dongliu.apk.parser.xlcw.configParam
import com.google.android.material.snackbar.Snackbar

private var toast: Toast? = null
private var snake: Snackbar? = null

fun toast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun snackBar(activity: Activity, msg: String, isLong: Boolean) {
    snackBar(activity.window.decorView, msg, isLong)
}

fun snackBar(rootView: View, msg: String, isLong: Boolean) {
    val snackBar = Snackbar.make(rootView, msg, if (isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT)
    snackBar.show()
}