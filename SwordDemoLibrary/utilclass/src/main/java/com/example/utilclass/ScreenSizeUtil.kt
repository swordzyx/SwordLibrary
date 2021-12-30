@file: JvmName("ScreenSize")

package com.example.utilclass

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.*
import androidx.annotation.RequiresApi

/**
 * 获取应用程序显示区域的尺寸
 */
fun getWindowSizeExcludeSystem(context: Context): Point {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    return getScreenSizeByMetrics(context)
  } else {
    return getScreenSizeByDisplay(context)
  }
}


/**
 * 单位转换，dp 转成 px
 */
fun dpToPx(dp: Int): Int {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    Resources.getSystem().displayMetrics
  ).toInt()
}

/**
 * 获取应用程序逻辑显示的尺寸
 */
private fun getScreenSizeByDisplay(context: Context): Point {
  val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  val display = windowManager.defaultDisplay
  return Point().apply {
    display.getSize(this)
  }
}


/**
 * 获取除去了系统导航栏以及显示器裁剪区域的显示尺寸，等价于 Display#getSize(Point)
 */
@RequiresApi(Build.VERSION_CODES.R)
private fun getScreenSizeByMetrics(context: Context): Point {
  val windowManager = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
  val metrics = windowManager.currentWindowMetrics

  val insets =
    metrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout())
  val insetsWidth = insets.left + insets.right
  val insetsHeight = insets.top + insets.bottom

  return Point(metrics.bounds.width() - insetsWidth, metrics.bounds.height() - insetsHeight)
}

/**
 * 获取 SurfaceView 显示区域的尺寸
 */
private fun getSurfaceViewSizeByDisplay(surfaceView: SurfaceView): Point {
  return Point().apply {
    surfaceView.display.getRealSize(this)
  }
}

/**
 * 单位转换，dp 转成 px
 */
/*
fun dpToPx(dp: Int): Int {
	return TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		dp.toFloat(),
		Resources.getSystem().displayMetrics
	).toInt()
}*/


/**
 * 判断华为手机是否有刘海
 */
fun isNotchScreenHW(window: Window): Boolean {
  val cl = window.context.classLoader
  val hwNotchSizeUtilCls = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
  val hasNotchInScreen = hwNotchSizeUtilCls.getMethod("hasNotchInScreen")
  return hasNotchInScreen.invoke(hwNotchSizeUtilCls) as Boolean
}

/**
 * 华为手机获取刘海屏高度
 */
fun getNotchHeightHw(window: Window): IntArray {
  if (!isNotchScreenHW(window)) {
    return intArrayOf(0, 0)
  }
  val ret: IntArray
  val cl = window.context.classLoader
  val hwNotchSizeUtilCls = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
  val getNotchSizeMethod = hwNotchSizeUtilCls.getMethod("getNotchSize")
  ret = getNotchSizeMethod.invoke(hwNotchSizeUtilCls) as IntArray
  return ret
}

/**
 * 在华为手机上设置使用刘海区域
 */
fun useNotchInFullScreenHw(window: Window) {
  val lp = window.attributes
  val layoutParamExCls =
    window.context.classLoader.loadClass("com.huawei.android.view.LayoutParamsEx")

  val con = layoutParamExCls.getConstructor(WindowManager.LayoutParams::class.java)
  val layoutParamExObj = con.newInstance(lp)

  val addHwFlagsMethod = layoutParamExCls.getMethod("addHwFlags", Int::class.java)
  addHwFlagsMethod.invoke(layoutParamExObj, FLAG_NOTCH_SUPPORT)
}

/**
 * 在华为手机上设置全屏不使用刘海区域
 */
fun excludeNotchInFullScreenHw(window: Window) {
  val layoutParamExCls =
    window.context.classLoader.loadClass("com.huawei.android.view.LayoutParamEx")

  val con = layoutParamExCls.getConstructor(WindowManager.LayoutParams::class.java)
  val layoutParamExObj = con.newInstance(window.attributes)

  val clearHwFlagsMethod = layoutParamExCls.getMethod("clearHwFlags", Int::class.java)
  clearHwFlagsMethod.invoke(layoutParamExObj, FLAG_NOTCH_SUPPORT)
}


/**
 * 获取小米设备是否为刘海屏设备
 */
@SuppressLint("PrivateApi")
fun isNotchScreenXiaomi(window: Window): Boolean {
  val systemPropertiesCls = window.context.classLoader.loadClass("android.os.SystemProperties")

  val getInstanceMethod = systemPropertiesCls.getMethod("getInstance")
  val systemPropertiesObj = getInstanceMethod.invoke(systemPropertiesCls)

  val getMethod = systemPropertiesCls.getMethod("get", String::class.java)
  val roMiniNotch = getMethod.invoke(systemPropertiesObj, "ro.miui.notch")
  return roMiniNotch == "1"
}

/**
 * 小米手机设置使用刘海区域
 *
 * 0x00000100 允许绘制到刘海区域
 * 0x00000200 竖屏配置
 * 0x00000400 横屏配置
 *
 * 组合使用：
 * 0x00000100 | 0x00000200 竖屏绘制到耳朵区
 * 0x00000100 | 0x00000400 横屏绘制到耳朵区
 * 0x00000100 | 0x00000200 | 0x00000400 横竖屏都绘制到耳朵区
 */
@RequiresApi(Build.VERSION_CODES.O)
fun useNotchInFullScreenXiaomi(activity: Activity) {
  if (!isNotchScreenXiaomi(activity.window)) {
    return
  }
  val flagNotch = 0x00000100 or 0x00000200 or 0x00000400
  val addExtraFlagsMethod = Window::class.java.getMethod("addExtraFlags", Int::class.java)
  addExtraFlagsMethod.isAccessible = true
  addExtraFlagsMethod.invoke(activity.window, flagNotch)
}

/**
 * 小米手机设置不使用刘海区域
 */
@RequiresApi(Build.VERSION_CODES.O)
fun excludeNotchInFullScreenXiaomi(activity: Activity) {
  if (!isNotchScreenXiaomi(activity.window)) {
    return
  }

  val flagNotch = 0x00000100 or 0x00000400
  val addExtraFlagMethod = Window::class.java.getMethod("addExtraFlags", Int::class.java)
  addExtraFlagMethod.isAccessible = true
  addExtraFlagMethod.invoke(activity.window, flagNotch)
}

/**
 * 获取状态栏高度
 */
fun getStatusHeight(context: Context): Int {
  val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
  return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
}

/**
 * 小米手机获取刘海屏尺寸
 */
fun getNotchHeightXiaomi(context: Context): Int {
  val resourceId = context.resources.getIdentifier("notch_height", "dimen", "android")
  return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
}

/**
 * 判断 oppo 是否为刘海屏手机
 */
fun isNotchScreenOppo(window: Window): Boolean {
  return window.context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
}

/**
 * oppo 手机获取刘海屏高度，官网给出的 OPPO 手机的刘海固定高度为 80px
 */
fun getNotchHeightOppo(window: Window): Int {
  if (!isNotchScreenOppo(window)) return 0
  return getStatusHeight(window.context)
}

/**
 * oppo 手机设置使用刘海区域，OPPO 手机在全屏状态下默认是占用刘海屏区域的，只需将应用设置为全屏沉浸式即可
 */
fun fullScreen(window: Window, setListener: Boolean) {
  /*var systemUiVisibility = 0
  //Android 4.1  
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
  }
  
  //Android 4.4  
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY 
  }
  
  window.decorView.systemUiVisibility = systemUiVisibility */

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    //假定 window 的按照全屏的模式布局（即隐藏状态栏和导航栏）
    window.setDecorFitsSystemWindows(false)
    //隐藏状态栏
    window.insetsController?.hide(WindowInsets.Type.statusBars())
    //隐藏导航栏
    window.insetsController?.hide(WindowInsets.Type.navigationBars())
  } else {
    val systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    window.decorView.systemUiVisibility = systemUiVisibility
  }
}


/**
 * 获取刘海区域的尺寸
 */
private fun gainCutoutRect(context: Context): List<Rect?>? {
  val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  var cutouts: List<Rect>? = null
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    val metric = windowManager.currentWindowMetrics
    val insets = metric.windowInsets
    val cutout = insets.displayCutout
    /*int insetTop = cutout.getSafeInsetTop();
          int insetBottom = cutout.getSafeInsetBottom();
          int insetLeft = cutout.getSafeInsetLeft();
          int insetRight = cutout.getSafeInsetRight();
          LogUtil.debug(tag, "30 insetTop: " + insetTop + "; insetBottom: " + insetBottom + "; insetLeft: " + insetLeft + "; insetRight: " + insetRight);*/cutouts =
      cutout!!.boundingRects
    for (r in cutouts) {
      LogUtil.debug(
        "bounding, left: " + r.left + "; top: " + r.top + "; right: " + r.right + "; bottom: " + r.bottom
      )
    }
  } else {
    val display = windowManager.defaultDisplay
    if (Build.VERSION.SDK_INT >= 29) {
      val cutout = display.cutout
      /*int insetTop = cutout.getSafeInsetTop();
              int insetBottom = cutout.getSafeInsetBottom();
              int insetLeft = cutout.getSafeInsetLeft();
              int insetRight = cutout.getSafeInsetRight();
              LogUtil.debug(tag, "29 insetTop: " + insetTop + "; insetBottom: " + insetBottom + "; insetLeft: " + insetLeft + "; insetRight: " + insetRight);*/
      cutouts =
        cutout!!.boundingRects
      for (r in cutouts) {
        LogUtil.debug(
          "bounding, left: " + r.left + "; top: " + r.top + "; right: " + r.right + "; bottom: " + r.bottom
        )
      }
    }
  }
  return rects
}


