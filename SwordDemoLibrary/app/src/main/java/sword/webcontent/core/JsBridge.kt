package sword.webcontent.core

import android.os.Handler
import android.os.Looper
import android.util.ArrayMap
import sword.SwordLog
import org.json.JSONException
import org.json.JSONObject

/**
 * 负责命令分发
 */
class JsBridgeInvokeDispatcher {
  companion object {
    private const val tag = "JsBridgeInvokeDispatcher"
    val instance = JsBridgeInvokeDispatcher()
  }
  
  fun sendCommand(view: BaseWebView, message: JsBridgeMessage) {
    SwordLog.debug(tag, "sendCommand, message: $message")
    if (checkMessage(message)) {
      executeCommand(view, message)
    }
  }

  /**
   * 校验桥接消息（JsBridgeMessage）是否合法
   */
  private fun checkMessage(message: JsBridgeMessage?): Boolean {
    if (message == null) {
      return false
    }
    
    return true
  }

  /**
   * 执行命令
   */
  private fun executeCommand(view: BaseWebView, message: JsBridgeMessage) {
    
  } 
}

/**
 * 注册命令，执行命令
 */
class JsBridgeCommandHandler {
  companion object {
    val instance = JsBridgeCommandHandler()
  }

  //主线程 Handler
  private val handler = Handler(Looper.getMainLooper())

  //保存命令
  private val commandMap by lazy {
    ArrayMap<String, JsBridgeCommand>().apply {
      put("showToast", ToastCommand())
    }
  }

  /**
   * 分发命令
   */
  fun handleBridgeInvoke(command: String?, params: String?) {
    if (command.isNullOrEmpty()) {
      return
    }
    if (commandMap.contains(command)) {
      handler.post {
        val paramJson = try {
          params?.let {
            JSONObject(params)
          }
        } catch (e: JSONException) {
          e.printStackTrace()
          null
        }
        commandMap[command]?.exec(paramJson)
      }
    }
  }
}

