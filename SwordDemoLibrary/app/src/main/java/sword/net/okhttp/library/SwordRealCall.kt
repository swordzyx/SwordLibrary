package sword.net.okhttp.library


class SwordRealCall(val client: SwordOkHttpClient, val reqeust: Request, val forWebSocket: Boolean) {
  private var callStackTrace: Any? = null
  private val eventListener: SwordEventListener = object : SwordEventListener() {
    
  }
  
  fun enqueue(callback: Callback) {
    callStart()
  }
  
  private fun callStart() {
    //在出错的时候去做出错分析用的，保存发起请求时所在的堆栈
    callStackTrace = Platform.get().getStackTraceForCloseable("response.body.close()")
    //通知 evenListener 开始发送请求
    eventListener.callStart(this)
  }
}