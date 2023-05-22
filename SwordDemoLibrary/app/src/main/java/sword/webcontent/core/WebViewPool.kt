package sword.webcontent.core

import android.content.Context
import android.content.MutableContextWrapper
import com.sword.LogUtil
import java.util.*

/**
 * WebView 池
 * 优点：
 *    1. 缩短初次创建的时间
 *    2. WebView 复用
 * 缺点：
 *    1. 在 Application#onCreate 中初始化 Webview 池会增加应用初始化耗时；
 *    2. 使用 Activity 初始化 webview 池会出现 activity 内存泄漏；使用一个 Context 包装类 MutableContextWrapper 可以解决，随时修改 MutableContextWrapper 中的时机 context
 */
open class WebViewPool private constructor() {
  val tag = "WebViewPool"
  companion object {
    val instance by lazy {
      WebViewPool()
    }
  }

  private val webviewPool = Stack<BaseWebView>()
  
  @Volatile
  private var maxSize = 1

  /**
   * 设置 webview 池容量
   */
  fun setMaxPoolSize(size: Int) {
    maxSize = size
  }

  /**
   * Webview 池初始化
   */
  fun init(context: Context, initSize: Int = maxSize) {
    for (i in 0 until initSize) {
      val view = BaseWebView(MutableContextWrapper(context)).apply {
        webViewClient = CustomWebviewClient()
        webChromeClient = CustomWebChromeClient()
      }
      webviewPool.push(view)
    }
  }

  /**
   * 获取 Webview
   */
  fun getWebview(context: Context): BaseWebView {
    val webview = if (webviewPool.size > 0) {
      LogUtil.debug(tag, "getWebview >> get from webviewPool")
      webviewPool.pop().apply {
        (this.context as MutableContextWrapper).baseContext = context
      }
    } else {
      LogUtil.debug(tag, "getWebview >> create new Webview")
      BaseWebView(MutableContextWrapper(context))
    }
    webview.webViewClient = CustomWebviewClient()
    webview.webChromeClient = CustomWebChromeClient()
    return webview
  }

  /**
   * 回收 Webview
   */
  fun recycle(webView: BaseWebView) {
    //始放 weview 所占用的资源
    webView.release()

    (webView.context as MutableContextWrapper).baseContext = webView.context.applicationContext
    if (webviewPool.size < maxSize) {
      webviewPool.push(webView)
    } else {
      webView.destroy()
    }
  }
}