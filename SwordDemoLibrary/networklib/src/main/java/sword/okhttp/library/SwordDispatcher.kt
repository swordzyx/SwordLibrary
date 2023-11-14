package sword.okhttp.library

import java.lang.AssertionError
import java.lang.IllegalArgumentException
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class SwordDispatcher constructor() {
  private val readyCallQueue = ArrayDeque<SwordRealCall.AsyncCall>()

  //允许外界设置线程池，但 OkHttp 内部必须要有一个不为空的线程池，这个线程池实例需要是线程安全的，或者说在 OkHttp 内部使用时需要确保是线程安全的
  private var executorServiceOrNull: ExecutorService? = null
  @get:Synchronized private val executorService: ExecutorService
    get() {
      if (executorServiceOrNull == null) {
        //默认线程池，可以开启无数个线程，核心线程数为 0
        executorServiceOrNull = ThreadPoolExecutor(0, Int.MAX_VALUE, 60, TimeUnit.SECONDS, SynchronousQueue(), threadFactory("okhttp dispatcher", false))
      }
      return executorServiceOrNull!!
    }
  

  constructor(executorService: ExecutorService): this() {
    this.executorServiceOrNull = executorService
  }
  
  //最大请求数
  @get:Synchronized
  var maxRequests = 64
    set(value) {
      if (value < 1) throw IllegalArgumentException("max < 1, $value")
      synchronized(this) {
        field = value
      }
    }

  //能够发往同一个主机的最大请求数
  @get:Synchronized
  var maxRequestPreHost = 5
    set(value) {
      if (value < 1) throw IllegalArgumentException("maxRequestPreHost < 1, $value")
      field = value
    }
  
  private val runningSyncCall = ArrayDeque<SwordRealCall>()
  @Synchronized internal fun execute(call: SwordRealCall) {
    runningSyncCall.add(call)
  }

  private val runningCallDeque = ArrayDeque<SwordRealCall.AsyncCall>()
  internal fun enqueue(call: SwordRealCall.AsyncCall): Boolean {
    
    
    synchronized(this) {
      //1. 将 call 添加到一个队列里面（先进先出）
      readyCallQueue.add(call)

      //2. 查找是否有一个可以重用的 Call，是如何判断一个 Call 是否可以被重用的，只有 HTTP 连接才可以重用，WebSocket 连接是无法重用的
    }

    //3. 执行所有可执行的 Call
    return promoteAndExecute()
  }
  
  private fun promoteAndExecute(): Boolean {
    val excutableCall = mutableListOf<SwordRealCall.AsyncCall>()
    var isRunning: Boolean
    synchronized(this) {
      //遍历队列中所有的 Call
      val i = readyCallQueue.iterator()
      while (i.hasNext()) {
        val asyncCall = i.next()
        //判断请求数有没有超，使用一个队列保存正在运行的 Call，判断这个队列的数量是否达到最大值
        if (runningCallDeque.size > maxRequests) break
        if (asyncCall.callsPerHost.get() >= maxRequestPreHost) continue
        i.remove()

        //将所有可以执行的 Call 放到队列中，等会执行
        asyncCall.callsPerHost.getAndIncrement()
        excutableCall.add(asyncCall)
        runningCallDeque.add(asyncCall)
      }
      isRunning = runningCallDeque.size > 0
    }

    //这一部分不必放在同步块中；因为同步块保护了 Dispater 维护的请求总数，确保这个请求总数是线程安全的，而 excutableCall 是基于 readyCallQueue 而生的，readyCallQueue 安全了，excutableCall 也就安全了。
    excutableCall.forEach { asyncCall ->
      //将 asyncCall 放到线程池上去执行
      asyncCall.executeOn(executorService)
    }
    return isRunning
  }
  internal fun finish(call: SwordRealCall.AsyncCall) {
    //网络请求结束，将 Call 从队列中移除
    synchronized(this) {
      if (!runningCallDeque.remove(call)) throw AssertionError("Call wasn't in-flight!")
    }
    //一个请求结束了，重新遍历一遍 Call 队列，让可执行的 Call 开始执行
    promoteAndExecute()
  }
}