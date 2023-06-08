package com.example.jetpackcompose.utils

import android.app.Activity
import androidx.window.layout.WindowMetricsCalculator
import com.sword.LogUtil

private const val tag = "Jetpack_ScreenSize" 

fun getScreenSizeByMetrics(activity: Activity) {
  //使用 WindowMetricsCalculator 须引入 “androidx.window:window:${version}” 依赖
  val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
  LogUtil.debug(tag, "windowMetrics: $windowMetrics")
}