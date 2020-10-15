@file:JvmName("Utils")
package com.sword.base.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.Toast
import com.sword.base.BaseApplication

private val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics

//包级函数
fun dp2px(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
}

fun toast(string: String?) {
    toast(string, Toast.LENGTH_SHORT)
}

fun toast(string: String?, duration: Int) {
    Toast.makeText(BaseApplication.currentApplication(), string, duration).show()
}

