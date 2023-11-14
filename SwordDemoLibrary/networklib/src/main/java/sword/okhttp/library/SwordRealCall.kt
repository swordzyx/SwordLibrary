package sword.okhttp.library

import sword.okhttp.library.connect.SwordExchangeFinder
import sword.okhttp.library.interceptor.SwordInterceptor
import sword.okhttp.library.interceptor.SwordInterceptorChain
import java.io.IOException
import java.io.InterruptedIOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.atomic.AtomicInteger


class SwordRealCall(val client: SwordOkHttpClient, val reqeust: SwordRequest, val forWebSocket: Boolean) {
  private var callStackTrace: Any? = null
  private val eventListener = client.eventListenerFactory.create(this)
  private var exchangeFinder: SwordExchangeFinder? = null
  
  fun enqueue(callback: Callback) {
    //保存当前所在的堆栈、回调 callStart 给 EventListener
    callStart()
    client.dispatcher.enqueue(AsyncCall(callback))
  }
  
  fun execute(): SwordResponse {
    callStart()
    client.dispatcher.execute(this)
    return getResponseWithInterceptorChain()
  }
  
  private fun callStart() {
    //在出错的时候去做出错分析用的，保存发起请求时所在的堆栈
    callStackTrace = Platform.get().getStackTraceForCloseable("response.body.close()")
    //通知 evenListener 开始发送请求
    client.eventListenerFactory.create(this).callStart(this)
  }
  
  fun enterNetworkInterceptorExchange(request: SwordRequest, newExchangeFinder: Boolean) {
    if (newExchangeFinder) {
      val address = SwordAddress()
      exchangeFinder = SwordExchangeFinder(client.connectionPool, this@SwordRealCall, address)
    }
  }

  internal inner class AsyncCall(
    private val callback: Callback
  ): Runnable {
    @Volatile var callsPerHost = AtomicInteger(0)
      private set
    
    fun reuseCallFromSameHostCall(call: AsyncCall) {
      this.callsPerHost = call.callsPerHost
    }
    
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
        callback.onFailure(this@SwordRealCall, ioe)
      }
    }
    
    //在线程池上执行
    fun executeOn(executorService: ExecutorService) {
      var success = false
      try {
        //开始执行
        executorService.execute(this)
        success = true
      } catch (e: RejectedExecutionException) {
        //Call 的执行请求被拒绝了
        val ioException = InterruptedIOException("executor reject")
        ioException.initCause(e)
        callback.onFailure(this@SwordRealCall, ioException)

        //todo：释放这个 Call 所占用的资源（连接、），通知 EventListener 这个 Call 请求结束
      } finally {
        //执行结束，将这个 call 从 dispatcher 的队列中移除
        if (!success) {
          client.dispatcher.finish(this)
        }
      }
      
      
    }
  }

  fun getResponseWithInterceptorChain(): SwordResponse {
    val interceptors = mutableListOf<SwordInterceptor>()
    interceptors += client.interceptors
    //todo: 增加请求和重试相关的 Interceptor
    //todo：增加 BridgeInterceptor，完善请求报文中的 Header
    //todo：增加缓存管理相关的 Interceptor
    //todo：建立连接相关的 Interceptor
    interceptors += client.networkInterceptors
    //todo: 往连接中些内容，从连接中读取内容
    val interceptorChain = SwordInterceptorChain(this, interceptors, 0, this.reqeust)
    
    try {
      val response = interceptorChain.proceed(this.reqeust)
      return response
    } catch (e: IOException) {
      //通知 EventListener 请求失败
      eventListener.callFailed(this, e)
      throw e
    } finally {
      //通知 EventListener 请求结束
      eventListener.callEnd(this)
    }
  }
}