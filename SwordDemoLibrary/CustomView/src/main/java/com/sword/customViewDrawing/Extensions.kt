package com.sword.customViewDrawing

import android.content.res.Resources
import android.util.TypedValue

//Resources.getSystem() 只能拿到系统相关的资源，拿不到应用内部的资源。屏幕密度及尺寸相关的信息是放在系统的资源里面的。单位的转换是跟应用的资源无关的，所有的应用使用的屏幕密度是一样的。
val Float.dp
		get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
