package sword.webcontent.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.swordlibrary.webcontent.defaultSetting
import com.google.gson.Gson
import sword.logger.SwordLog
import org.json.JSONException
import sword.thread.ThreadExecutor

@SuppressLint("JavascriptInterface")
open class BaseWebView(context: Context, attrs: AttributeSet?) : WebView(context, attrs), LifecycleEventObserver {
  private val tag = "BaseWebView"
  
  interface BlankMonitorCallback {
    fun onBlank()
  }

  private var blankMonitorCallback: BlankMonitorCallback? = null

  init {
    setWebContentsDebuggingEnabled(true)
    isVerticalScrollBarEnabled = false
    isHorizontalScrollBarEnabled = false

    defaultSetting()

    //JS 对象注入，默认使用对象注入的方式实现 Android 端调用 JS 端的方法
    addJavascriptInterface(this, "bridge")
  }

  constructor(context: Context) : this(context, null)

  fun setBlankMonitorCallback(callback: BlankMonitorCallback) {
    blankMonitorCallback = callback
  }

  override fun getUrl(): String? {
    return super.getOriginalUrl() ?: super.getUrl()
  }

  override fun canGoBack(): Boolean {
    val backForwardList = copyBackForwardList()
    val currentIndex = backForwardList.currentIndex
    if (backForwardList.currentIndex - 1 >= 0) {
      val item = backForwardList.getItemAtIndex(currentIndex)
      if (item.url == "about:blank") {
        return false
      }
    }
    return super.canGoBack()
  }

  //监听 WebView 所在组件的生命周期
  fun setLifecycleOwner(owner: LifecycleOwner) {
    owner.lifecycle.addObserver(this)
  }
  
  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    when (event) {
      Lifecycle.Event.ON_RESUME -> onResume()
      Lifecycle.Event.ON_STOP -> onPause()
      Lifecycle.Event.ON_DESTROY -> {
        source.lifecycle.removeObserver(this)
        onDestroy()
      }
      else -> {}
    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun onResume() {
    super.onResume()
    settings.javaScriptEnabled = true
  }

  private fun onDestroy() {
    settings.javaScriptEnabled = false
  }

  /**
   * 释放 WebView 资源
   */
  open fun release() {
    (parent as ViewGroup?)?.removeView(this)
    removeAllViews()
    stopLoading()
    setCustomWebViewClient(null)
    setCustomWebChromeClient(null)
    loadUrl("about:blank")
    clearHistory()
  }

  //TODO：可以使用协程的延时和取消实现，delay 函数
  fun postBlankMonitorRunnable() {
    SwordLog.debug(tag, "5s 后执行白屏检测")
    postDelayed(blankMonitorExecRunnable, 5000)
  }

  fun cancelBlankMonitorRunnable() {
    SwordLog.debug(tag, "白屏检测任务取消执行")
    removeCallbacks(blankMonitorExecRunnable)
  }

  /**
   * Web 调用 Android 端函数
   */
  @JavascriptInterface
  fun sendCommand(json: String?) {
    if (json.isNullOrEmpty()) return
    
    try {
      SwordLog.debug(tag, "receive command from web: $json")
      val message = Gson().fromJson(json, JsBridgeMessage::class.java)
      
    } catch (e: JSONException) {
      e.printStackTrace()
    }
  }

  /**
   * Android 调用 Javascript 方法
   */
  fun callJavascript() {
    evaluateJavascript("javascript:showToast('hello world')") {}
  }

  private fun setCustomWebViewClient(webviewClient: CustomWebviewClient?) {
    if (webviewClient == null) {
      super.setWebViewClient(WebViewClient())
    } else {
      super.setWebViewClient(webviewClient)
    }
  }

  private fun setCustomWebChromeClient(webChromeClient: CustomWebChromeClient?) {
    setWebChromeClient(webChromeClient)
  }
  
  private val blankMonitorExecRunnable by lazy {
    val runnable = Runnable {
      SwordLog.debug(tag, "-------- 开始执行白屏检测 ----------------")
      val startTime = System.currentTimeMillis()
      val dstWidth = measuredWidth / 6
      val dstHeight = measuredHeight / 6
      val snapshot = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888)
      val canvas = Canvas(snapshot)
      draw(canvas)

      val pixelCount = (snapshot.width * snapshot.height).toFloat()
      var whitePixelCount = 0
      var otherPixelCount = 0
      for (x in 0 until snapshot.width) {
        for (y in 0 until snapshot.height) {
          if (snapshot.getPixel(x, y) == -1) {
            whitePixelCount++
          } else {
            otherPixelCount++
          }
        }
      }
      snapshot.recycle()

      if (whitePixelCount == 0) {
        return@Runnable
      }

      val percentage = whitePixelCount / pixelCount * 100
      if (percentage > 95) {
        //在主线程中回调 blankMonitorCallback
        post {
          blankMonitorCallback?.onBlank()
        }
        SwordLog.debug(tag, "---------------- 白屏检测结束，耗时：${System.currentTimeMillis() - startTime}s --------------------")
      }
    }
    Runnable {
      ThreadExecutor.getThreadExecutor().execute(runnable)
    }
  }
    
}