package com.example.utilclass

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.util.TypedValue
import android.view.SurfaceView
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import java.lang.reflect.TypeVariable

class ScreenSizeUtil {

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
    fun dpToPxe(dp: Int) : Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics).toInt()
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
    
    


}