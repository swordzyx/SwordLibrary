@file:JvmName("Utils")
package com.sword.base.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.Toast
import com.sword.base.BaseApplication

private val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics

//包级函数
fun Float.dp2px(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)
}


@JvmOverloads
fun toast(string: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(BaseApplication.currentApplication, string, duration).show()
}

