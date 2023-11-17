@file: JvmName("ScreenSize")
@file:Suppress("DEPRECATION")

package sword

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.os.Build
import android.util.TypedValue
import android.view.*
import androidx.annotation.RequiresApi
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import kotlinx.coroutines.*
import sword.logger.SwordLog

private const val tag = "ScreenSizeUtil"

private val windowSize: Point = getWindowSizeExcludeSystem(SwordApplication.globalContext!!) 

val windowWidth: Int
  get() = windowSize.x
val windowHeight: Int
  get() = windowSize.y




fun initWindowSize(context: Context) {
  val point = getWindowSizeExcludeSystem(context)
  windowSize.x = point.x
  windowSize.y = point.y
  SwordLog.debug(tag, "screenWidth: ${windowSize.x}, screentHeight: ${windowSize.y}")
}

fun publicApiTest(activity: Activity, window: Window) {
  val point: Point = getWindowSizeExcludeSystem(activity)
  SwordLog.debug(tag, "屏幕尺寸: ${point.x} - ${point.y}")

  notchScreenAdapte(activity, window, true)
}


fun test(activity: Activity) {
  var point: Point
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    point = getScreenSizeByMetrics(activity)
    SwordLog.debug(tag, "getScreenSizeByMetrics: ${point.x} - ${point.y}")
  } else {
    SwordLog.debug(tag, "Android version smaller than 11, ignore getScreenSizeByMetrics(...)")
  }
  point = getScreenSizeByDisplay(activity)
  SwordLog.debug(tag, "getScreenSizeByDisplay: ${point.x} - ${point.y}")
  
  getCutoutRect(activity)?.forEachIndexed { index, rect ->  
    SwordLog.debug(tag, "getCutoutRect-$index, rect: $rect")
  }

  printDisplayInfo(activity)
  
  //启动一个协程
  val scope = CoroutineScope(Dispatchers.Main)
  scope.launch { 
    printFlodInfo(activity)
  }
}

/**
 * 获取应用程序显示区域的尺寸
 */
fun getWindowSizeExcludeSystem(context: Context): Point {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    getScreenSizeByMetrics(context)
  } else {
    getScreenSizeByDisplay(context)
  }
}

/**
 * 刘海屏适配
 */
fun notchScreenAdapte(context: Context, window: Window, useNotch: Boolean) {
  if (isNotchScreenHW(window)) {
    getNotchHeightHw(window).forEach {
      SwordLog.debug(tag, "getNotchHeightHw: $it")
    }
    
    if (useNotch) useNotchInFullScreenHw(window) else excludeNotchInFullScreenHw(window) 
    
    return
  }
  
  if (isNotchScreenXiaomi(window)) {
    SwordLog.debug(tag, "getNotchHeightXiaomi: ${getNotchHeightXiaomi(context)}")
    if (useNotch) useNotchInFullScreenXiaomi(window) else  excludeNotchInFullScreenXiaomi(window)
    return
  }

  //oppo 手机设置使用刘海区域，OPPO 手机在全屏状态下默认是占用刘海屏区域的，只需将应用设置为全屏沉浸式即可
  if (isNotchScreenOppo(window)) {
    SwordLog.debug(tag, "getNotchHeightOppo: ${getNotchHeightOppo(window)}")
    if (useNotch) fullScreenByFlag(window)
  }
}

fun fullScreenByFlag(window: Window) {
  val systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

  window.decorView.systemUiVisibility = systemUiVisibility
}

/**
 * 设置全屏
 */
@RequiresApi(Build.VERSION_CODES.R)
fun fullScreenByInsetController(window: Window) {
  //假定 window 的按照全屏的模式布局（即隐藏状态栏和导航栏）
  window.setDecorFitsSystemWindows(false)
  //隐藏状态栏
  window.insetsController?.hide(WindowInsets.Type.statusBars())
  //隐藏导航栏
  window.insetsController?.hide(WindowInsets.Type.navigationBars())
}

/**
 * 单位转换，dp 转成 px
 * 
 * 返回的值其实是：dp * density（屏幕密度缩放因子，当前屏幕密度/标准屏幕密度（160dpi））
 */
fun Int.dp(): Int {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
  ).toInt()
}

/**
 * 获取状态栏高度
 */
@SuppressLint("InternalInsetResource")
fun getStatusHeight(context: Context): Int {
  val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
  return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
}

/**
 * 打印设备屏幕信息
 */
private fun printDisplayInfo(activity: Activity) {

  SwordLog.debug(tag, "onCreate configuration: " + activity.resources.configuration)
  
  SwordLog.debug(tag, "--------------------- DisplayManager.getDisplays --------------------")
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
    val dm = activity.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    for (d: Display in dm.displays) {
      SwordLog.debug(tag, d.toString())
    }
  } else {
    SwordLog.debug(tag, "Android 系统版本小于 4.2（API 17），不支持 Display Api")
  }
  SwordLog.debug(tag, "--------------------- DisplayManager.getDisplays --------------------")
}


/**
 * 使用 Jetpack 的 WindowManager 库中的 WindowInfoTracker 获取折叠屏的状态
 * 
 * 需要引入 androidx.window:window:1.0.0 依赖库
 * 如果使用 Java，则须引入：androidx.window:window-java:1.0.0
 */
private suspend fun printFlodInfo(activity: Activity) {
  val windowInfoTracker = WindowInfoTracker.getOrCreate(activity)

  windowInfoTracker.windowLayoutInfo(activity).collect { windowLayoutInfo ->
    SwordLog.debug(tag, "LayoutStateChange: $windowLayoutInfo")

    for (df in windowLayoutInfo.displayFeatures) {
      if (df is FoldingFeature) {
        SwordLog.debug(tag, "Folding State")
        SwordLog.debug(tag, "State: " + (df as FoldingFeature).state)
        SwordLog.debug(tag, "OcclusionType: " + (df as FoldingFeature).occlusionType)
        SwordLog.debug(tag, "Orientation: " + (df as FoldingFeature).orientation)
      }
    }
  }
}

/**
 * 通过 Display 获取应用程序逻辑显示的尺寸（Android 11 丢弃）
 */
private fun getScreenSizeByDisplay(context: Context): Point {
  val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    context.display
  } else {
    (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
  }

  return Point().apply {
    display!!.getSize(this)
  }
}

/**
 * 通过 WindowMetrics 获取屏幕尺寸（Android 12 推荐的获取屏幕尺寸的方式）
 * 获取除去了系统导航栏以及显示器裁剪区域的显示尺寸，等价于 Display#getSize(Point)
 */
@RequiresApi(Build.VERSION_CODES.R)
private fun getScreenSizeByMetrics(context: Context): Point {
  val metrics = if (context is Activity) {
      context.windowManager.currentWindowMetrics
    } else {
      (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).currentWindowMetrics
    }

  //metrics.windowInsets 获取屏幕所有装饰物的尺寸信息
  val insets = metrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout())
  val insetsWidth = insets.left + insets.right
  val insetsHeight = insets.top + insets.bottom
  SwordLog.debug("ScreenSizeUtil", "insets: $insets")
  SwordLog.debug("ScreenSizeUtil", "metrics: ${metrics.bounds}")
  
  val insets1 = metrics.windowInsets.getInsets(0)
  SwordLog.debug("ScreenSizeUtil", "insets1, $insets1")

  return Point(metrics.bounds.width() - insetsWidth, metrics.bounds.height() - insetsHeight)
}

/**
 * 使用 Display 获取 SurfaceView 显示区域的尺寸，Android 11 丢弃
 */
private fun getSurfaceViewSizeByDisplay(surfaceView: SurfaceView): Point {
  return Point().apply {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      surfaceView.display.getRealSize(this)
    } else {
      x = -1
      y = -1
    }
  }
}

/**
 * 判断华为手机是否有刘海
 */
private fun isNotchScreenHW(window: Window): Boolean {
  val cl = window.context.classLoader
  try {
    val hwNotchSizeUtilCls = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
    val hasNotchInScreen = hwNotchSizeUtilCls.getMethod("hasNotchInScreen")
    return hasNotchInScreen.invoke(hwNotchSizeUtilCls) as Boolean
  } catch (e: java.lang.Exception) {
    e.printStackTrace()
  }
  return false
}

/**
 * 华为手机获取刘海屏高度
 */
private fun getNotchHeightHw(window: Window): IntArray {
  if (!isNotchScreenHW(window)) {
    return intArrayOf(0, 0)
  }
  try {
    val ret: IntArray
    val cl = window.context.classLoader
    val hwNotchSizeUtilCls = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
    val getNotchSizeMethod = hwNotchSizeUtilCls.getMethod("getNotchSize")
    ret = getNotchSizeMethod.invoke(hwNotchSizeUtilCls) as IntArray
    return ret
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return intArrayOf(0, 0)
}

/**
 * 在华为手机上设置使用刘海区域
 */
private const val FLAG_NOTCH_SUPPORT = 1
private fun useNotchInFullScreenHw(window: Window) {
  
  try {
    val lp = window.attributes

    val layoutParamExCls =
      window.context.classLoader.loadClass("com.huawei.android.view.LayoutParamsEx")

    val con = layoutParamExCls.getConstructor(WindowManager.LayoutParams::class.java)
    val layoutParamExObj = con.newInstance(lp)

    val addHwFlagsMethod = layoutParamExCls.getMethod("addHwFlags", Int::class.java)
    addHwFlagsMethod.invoke(layoutParamExObj, FLAG_NOTCH_SUPPORT)
  } catch (e: Exception) {
    e.printStackTrace()
  }
}

/**
 * 在华为手机上设置全屏不使用刘海区域
 */
private fun excludeNotchInFullScreenHw(window: Window) {
  try {
    val layoutParamExCls =
      window.context.classLoader.loadClass("com.huawei.android.view.LayoutParamEx")

    val con = layoutParamExCls.getConstructor(WindowManager.LayoutParams::class.java)
    val layoutParamExObj = con.newInstance(window.attributes)

    val clearHwFlagsMethod = layoutParamExCls.getMethod("clearHwFlags", Int::class.java)
    clearHwFlagsMethod.invoke(layoutParamExObj, FLAG_NOTCH_SUPPORT)
  } catch (e: Exception) {
    e.printStackTrace()
  }
}


/**
 * 获取小米设备是否为刘海屏设备
 */
@SuppressLint("PrivateApi")
private fun isNotchScreenXiaomi(window: Window): Boolean {
  try {
    val systemPropertiesCls = window.context.classLoader.loadClass("android.os.SystemProperties")

    val getInstanceMethod = systemPropertiesCls.getMethod("getInstance")
    val systemPropertiesObj = getInstanceMethod.invoke(systemPropertiesCls)

    val getMethod = systemPropertiesCls.getMethod("get", String::class.java)
    val roMiniNotch = getMethod.invoke(systemPropertiesObj, "ro.miui.notch")
    return roMiniNotch == "1"
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return false
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
private fun useNotchInFullScreenXiaomi(window: Window) {
  if (!isNotchScreenXiaomi(window)) {
    return
  }
  
  try {
    val flagNotch = 0x00000100 or 0x00000200 or 0x00000400
    val addExtraFlagsMethod = Window::class.java.getMethod("addExtraFlags", Int::class.java)
    addExtraFlagsMethod.isAccessible = true
    addExtraFlagsMethod.invoke(window, flagNotch)
  } catch (e: Exception) {
    e.printStackTrace()
  }
}

/**
 * 小米手机设置不使用刘海区域
 */
private fun excludeNotchInFullScreenXiaomi(window: Window) {
  if (!isNotchScreenXiaomi(window)) {
    return
  }

  try {
    val flagNotch = 0x00000100 or 0x00000400
    val addExtraFlagMethod = Window::class.java.getMethod("addExtraFlags", Int::class.java)
    addExtraFlagMethod.isAccessible = true
    addExtraFlagMethod.invoke(window, flagNotch)
  } catch (e: Exception) {
    e.printStackTrace()
  }
}



/**
 * 小米手机获取刘海屏尺寸
 */
private fun getNotchHeightXiaomi(context: Context): Int {
  val resourceId = context.resources.getIdentifier("notch_height", "dimen", "android")
  return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
}

/**
 * 判断 oppo 是否为刘海屏手机
 */
private fun isNotchScreenOppo(window: Window): Boolean {
  return window.context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
}

/**
 * oppo 手机获取刘海屏高度，官网给出的 OPPO 手机的刘海固定高度为 80px
 */
private fun getNotchHeightOppo(window: Window): Int {
  if (!isNotchScreenOppo(window)) return 0
  return getStatusHeight(window.context)
}

/**
 * 获取刘海区域的尺寸
 */
private fun getCutoutRect(context: Context): List<Rect?>? {
  val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  val cutouts: List<Rect>? = null
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {  //Android 11
    val cutout = windowManager.currentWindowMetrics.windowInsets.displayCutout ?: return null
    SwordLog.debug(tag, "windowInsets.displayCutout: $cutout")
    
    cutout.boundingRects.forEachIndexed { index, r ->
      SwordLog.debug("bounding-$index, left: " + r.left + "; top: " + r.top + "; right: " + r.right + "; bottom: " + r.bottom)
    }
  } else {
    if (Build.VERSION.SDK_INT >= 29) { // Android 10 
      val cutout = windowManager.defaultDisplay.cutout ?: return null
      SwordLog.debug(tag, "WindowManager.defaultDisplay.cutout: $cutout")
      cutout.boundingRects.forEachIndexed { index, r ->
        SwordLog.debug("bounding-$index, left: " + r.left + "; top: " + r.top + "; right: " + r.right + "; bottom: " + r.bottom)
      }
    }
  }
  return cutouts
}


