package com.example.swordlibrary.webcontent.core

import android.os.Handler
import android.os.Looper
import android.util.ArrayMap
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.sword.LogUtil
import com.sword.SwordApplication
import com.sword.toast
import org.json.JSONException
import org.json.JSONObject

/**
 * 前端与 Android 交互使用的消息数据类型
 */
data class JsBridgeMessage(@SerializedName("command") val command: String?, @SerializedName("params") val params: JSONObject?)

/**
 * 由 JsBridgeMessage.params 转化而来，表示前端和 Android 交互的指令
 */
interface JsBridgeCommand {
  fun exec(params: JSONObject?)
}

/**
 * Toast 相关命令
 */
class ToastCommand : JsBridgeCommand {
  override fun exec(params: JSONObject?) {
    val msg = params?.optString("message")
    LogUtil.debug("showToast, msg: $msg")
    msg?.let {
      toast(SwordApplication.getGlobalContext(), it)
    }
  }
}

/**
 * 负责命令分发
 */
class JsBridgeInvokeDispatcher {
  companion object {
    private const val tag = "JsBridgeInvokeDispatcher"
    val instance = JsBridgeInvokeDispatcher()
  }
  
  fun sendCommand(view: BaseWebView, message: JsBridgeMessage) {
    LogUtil.debug(tag, "sendCommand, message: $message")
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
  
  private fun executeCommand(view: BaseWebView, message: JsBridgeMessage) {
    
  } 
}

class JsBridgeCommandHandler {
  companion object {
    val instance = JsBridgeCommandHandler()
  }
  
  private val handler = Handler(Looper.getMainLooper())
  
  private val commandMap by lazy {
    ArrayMap<String, JsBridgeCommand>().apply { 
      put("showToast", ToastCommand())
    }
  }
  
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

