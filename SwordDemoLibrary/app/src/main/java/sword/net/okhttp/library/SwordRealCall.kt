package sword.net.okhttp.library

import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger


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

  internal inner class AsyncCall(
    private val callback: Callback
  ): Runnable {
    @Volatile var callsPerHost = AtomicInteger(0)
      private set
    override fun run() {
      try {
        //1. 发送网络请求
        val response = getResponseWithInterceptorChain()

        //2. 将请求结果回调给 callback
        callback.onResponse(this@SwordRealCall, response)
      } catch (ioe: IOException) {
        callback.onFailure(this@SwordRealCall, ioe)
      } catch (th: Throwable) {
        val ioe = IOException("cancel due to $th")
        ioe.addSuppressed(th)
        callback.onResponse(this@SwordRealCall, ioe)
      }

    }
  }


}