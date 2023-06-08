package sword.thread

import android.os.Handler
import android.os.Looper

/**
 * 主线程执行器
 */
class MainThreadExecutor {
  private val handler = Handler(Looper.getMainLooper())
  
  fun execute(r: Runnable) {
    handler.post(r)
  }
}