package com.example.swordlibrary.webcontent

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.swordlibrary.webcontent.core.BaseWebView
import com.example.swordlibrary.webcontent.core.WebViewPool

class WebViewFragment(val context: Context) {
  val webview = WebViewPool.instance.getWebview(context)

  @SuppressLint("JavascriptInterface")
  fun createView(): View {
    val contentView = LinearLayout(context).apply {
      addView(
        webview, LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.MATCH_PARENT
        )
      )
    }
    
    webview.setBlankMonitorCallback(object: BaseWebView.BlankMonitorCallback {
      override fun onBlank() {
        AlertDialog.Builder(context)
          .setTitle("提示")
          .setMessage("检测到页面加载发生异常，是否重新加载")
          .setPositiveButton("重新加载") { dialog, _ -> 
            dialog.dismiss()
            webview.reload()
          }
          .setNegativeButton("返回上一页") { dialog, _ -> 
            dialog.dismiss()
            onBackPressed()
          }
          .create()
          .show()
      }
    })
    
    return contentView
  }
  
  fun onBackPressed() {
    if (webview.canGoBack()) {
      webview.goBack()
    }
  }
  
  fun loadUrl(url: String?) {
    if (TextUtils.isEmpty(url)) {
      webview.loadUrl("about:blank")
    }
    webview.loadUrl(url!!)
  }
}