package com.example.swordlibrary.webcontent.core

import android.content.Context
import android.content.MutableContextWrapper
import java.util.*

class TemplateWebViewPool{
  val tag = "TemplateWebViewPool"
  
  companion object {
    val instance by lazy { 
      TemplateWebViewPool()
    }
  }
  
  private val tempWebviewPool = Stack<BaseWebView>()
  
  @Volatile
  private var maxSize = 1
  
  fun setMaxPoolSize(size: Int) {
    maxSize = size
  }
  
  fun init(context: Context, initSize: Int = maxSize) {
    for (i in 0 until initSize) {
      tempWebviewPool.push(TemplateWebView(MutableContextWrapper(context.applicationContext)).apply {
        loadUrl("file:///android_asset/template_news.html")
      })
    }
  }
}