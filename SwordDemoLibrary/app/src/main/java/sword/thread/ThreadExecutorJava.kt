package sword.thread

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * 线程调度器
 */
class ThreadExecutorJava {
  companion object {
    /*private val executor by lazy {
      Executors.newCachedThreadPool()
    }

    fun execute(runnable: Runnable) {
      executor.submit(runnable)
    }*/
    
    private val scheduleExecutor by lazy {
      Executors.newSingleThreadScheduledExecutor()
    }
    
    fun execute(runnable: Runnable) {
      execute(0, runnable)
    }
    
    fun execute(delay: Int, runnable: Runnable) {
      scheduleExecutor.schedule(runnable, delay.toLong(), TimeUnit.SECONDS)
    }
    
  }
}

