package com.example.learnkotlin.utils

import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import com.example.learnkotlin.core.BaseApplication

private val displayMetrics = Resources.getSystem().displayMetrics

/**
 * applyDimension 用于将不同尺寸单位的值转换成 px 值，第一个参数是要转换的单位。
 */
fun dp2px(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)

fun toast(string: String) {
    toast(string, Toast.LENGTH_SHORT)
}

fun toast(string: String, duration: Int) {
    Toast.makeText(BaseApplication.currentApplication(), string, duration).show()
}