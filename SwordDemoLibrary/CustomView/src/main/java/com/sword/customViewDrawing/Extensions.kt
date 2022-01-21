package com.sword.customViewDrawing

import android.content.res.Resources
import android.util.TypedValue

//Resources.getSystem() 只能拿到系统相关的资源，拿不到应用内部的资源。屏幕密度及尺寸相关的信息是放在系统的资源里面的。
fun Float.dp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics) 
