@file: JvmName("WebViewUtil")
package com.example.swordlibrary.webcontent

import android.content.Context
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.example.swordlibrary.R
import java.io.File

/**
 * WebView 缓存文件路径
 */
fun getWebViewCachePath(context: Context): String {
  return context.filesDir.absolutePath + File.pathSeparator + "webCache"
}

/**
 * WebView 默认设置
 */
fun WebView.defaultSetting() {
  //WebView 背景设置，执行 View.setBackgroundColor(...)
  //setBackgroundColor(Color.TRANSPARENT)
  //执行 View.setBackgroundResource(...)
  setBackgroundResource(R.color.color_gray)
  
  //不允许滑动
  overScrollMode = View.OVER_SCROLL_NEVER
  //不允许嵌套滑动
  isNestedScrollingEnabled = false
  
  //自适应屏幕
  settings.useWideViewPort = true
  settings.loadWithOverviewMode = true
  
  //不支持支持缩放
  settings.setSupportZoom(false)
  //不使用内置的缩放控件
  settings.builtInZoomControls = false
  //不显示原生缩放控件
  settings.displayZoomControls = false
  //文本缩放，默认为 100，表示不缩放
  settings.textZoom = 100
  
  //不保存密码 
  settings.savePassword = false
  //可以访问文件
  settings.allowFileAccess = true
  //可以通过 JS 打开新的窗口 
  settings.javaScriptCanOpenWindowsAutomatically = true
  
  //支持自动加载图片
  settings.loadsImagesAutomatically = true
  //允许通过网络下载图片。loadsImageAutomatically 为 true 时，此配置才有意义；
  settings.blockNetworkImage = false
  
  //设置编码格式为 utf-8
  settings.defaultTextEncodingName = "UTF-8"
  //设置为常规布局，不对网页做额外的操作
  settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
  //启用 database storeage api 功能
  settings.domStorageEnabled = true
  //允许 WebView 从不安全的源加载 Resource
  settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
  
  //设置缓存模式。
  settings.cacheMode = WebSettings.LOAD_DEFAULT
  //开启 Application Cache，默认为不开启，targetSdkVersion 为 33 时，WebSettings.setAppCacheEnabled(...) 这个 API 找不到，应该是 Google 移除了。
  //Application Cache API 已被弃用，Chromium 中可能会移除这些 API，一旦这些 API 被移除，这些方法会成为空操作方法
  settings.setAppCacheEnabled(true)
  //设置缓存目录
  val cachePath = getWebViewCachePath(context)
  val cacheDir = File(cachePath)
  if (!cacheDir.exists() && !cacheDir.isDirectory) {
    cacheDir.mkdir()
  }
  settings.setAppCachePath(cachePath)
}