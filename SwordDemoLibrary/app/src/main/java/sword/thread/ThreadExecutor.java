package sword.thread;

import sword.logger.SwordLog;

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

  /**
   * 创建一个核心线程数固定的线程池，即至少会有这个数量的线程可拿来用
   */
  public static Executor getThreadExecutor() {
    if (threadExecutor == null || (threadExecutor instanceof ThreadPoolExecutor) && (((ThreadPoolExecutor) threadExecutor).isShutdown()) || ((ThreadPoolExecutor) threadExecutor).isTerminated() || ((ThreadPoolExecutor) threadExecutor).isTerminating()) {
      threadExecutor = Executors.newFixedThreadPool(coreThreadSize);
    }
    return threadExecutor;
  }

  /**
   * 创建一个可执行延时、定时等任务的线程池
   */
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
          SwordLog.error(tag, "killing non-finished task");
        }
        executor.shutdown();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  } 
}
