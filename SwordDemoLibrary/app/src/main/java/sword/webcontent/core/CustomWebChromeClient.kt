package sword.webcontent.core

import android.webkit.*
import androidx.appcompat.app.AlertDialog
import com.sword.LogUtil

class CustomWebChromeClient : WebChromeClient() {
  private val TAG = "SwordWebChromeClient"

  /**
   * 网页端控制台打印
   */
  override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
    LogUtil.debug(TAG, "onConsoleMessage -> ${consoleMessage.message()}")
    return super.onConsoleMessage(consoleMessage)
  }

  /**
   * 网页警告弹窗，无法返回内容给网页端
   */
  override fun onJsAlert(
    view: WebView,
    url: String,
    message: String,
    result: JsResult
  ): Boolean {
    LogUtil.debug(TAG, "url: $url, message：$message" )
    AlertDialog.Builder(view.context).setTitle("警告").setMessage(message)
      .setPositiveButton("确认") {dialog, which -> 
        dialog.dismiss()
        result.confirm()
      }
      .setNegativeButton("取消") { dialog, which ->
        dialog.dismiss()
        result.cancel()
      }
      .show()
    return true
  }

  /**
   * 网页弹出确认弹窗，只能返回 true 或者 false 给网页端
   */
  override fun onJsConfirm(
    view: WebView,
    url: String,
    message: String,
    result: JsResult
  ): Boolean {
    LogUtil.debug(TAG, "url: $url, message: $message")
    AlertDialog.Builder(view.context)
      .setTitle("警告")
      .setMessage(message)
      .setPositiveButton("确认") { dialog, which ->
        dialog.dismiss()
        result.confirm()
      }
      .setNegativeButton("取消") { dialog, which -> 
        dialog.dismiss()
        result.cancel()
      }
      .show()
    return true
  }

  /**
   * 弹出消息提示框，可以返回 String 类型的消息给网页端
   */
  override fun onJsPrompt(
    view: WebView,
    url: String,
    message: String,
    defaultValue: String,
    result: JsPromptResult
  ): Boolean {
    LogUtil.debug(TAG, "url: $url, message: $message, defaultValue: $defaultValue")
    result.confirm("onJsPrompt result")
    return true
  }
}