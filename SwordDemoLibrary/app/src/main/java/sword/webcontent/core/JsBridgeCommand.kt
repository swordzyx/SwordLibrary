package sword.webcontent.core

import com.google.gson.annotations.SerializedName
import com.sword.LogUtil
import sword.SwordApplication
import com.sword.toast
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
            toast(SwordApplication.globalContext, it)
        }
    }
}