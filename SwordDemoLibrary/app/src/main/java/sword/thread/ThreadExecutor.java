package sword.thread;

import com.sword.LogUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor {
  private static final String tag = "ThreadExecutor";
  private static final int coreThreadSize = 2;

  private static ScheduledThreadPoolExecutor scheduledThreadExecutor;
  private static Executor threadExecutor;

  public static Executor getThreadExecutor() {
    if (threadExecutor == null || (threadExecutor instanceof ThreadPoolExecutor) && (((ThreadPoolExecutor) threadExecutor).isShutdown()) || ((ThreadPoolExecutor) threadExecutor).isTerminated() || ((ThreadPoolExecutor) threadExecutor).isTerminating()) {
      threadExecutor = Executors.newFixedThreadPool(coreThreadSize);
    }
    return threadExecutor;
  }

  public static ScheduledThreadPoolExecutor getScheduledThreadExecutor() {
    if (scheduledThreadExecutor == null || scheduledThreadExecutor.isTerminating() || scheduledThreadExecutor.isTerminated() || scheduledThreadExecutor.isShutdown()) {
      scheduledThreadExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(coreThreadSize, Executors.defaultThreadFactory());
    }
    return scheduledThreadExecutor;
  }
  
  public static void shutdownExecutor(ExecutorService executor) {
    executor.shutdown();
    try {
      boolean terminated = executor.awaitTermination(10L, TimeUnit.SECONDS);
      if (!terminated) {
        if (!executor.isTerminated()) {
          LogUtil.error(tag, "killing non-finished task");
        }
        executor.shutdown();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  } 
}
