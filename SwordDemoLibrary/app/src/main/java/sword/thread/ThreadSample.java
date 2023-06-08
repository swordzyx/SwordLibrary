package sword.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 扔物线课程 - 多线程和线程同步课程代码
 */
public class ThreadSample {

  public static void main(String[] args) {
    threadFactory();
  }
  public static void threadFactory() {
    ThreadFactory threadFactory = new ThreadFactory() {
      private final AtomicInteger count = new AtomicInteger(1);
      @Override
      public Thread newThread(Runnable r) {
        return new Thread(r, "Thread-" + count.getAndIncrement());
      }
    };
    
    Runnable runnable = () -> System.out.println(Thread.currentThread().getName() + " Started~~");
    threadFactory.newThread(runnable).start();
    threadFactory.newThread(runnable).start();
  }
  
  public static void callable() {
    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.submit()
  }
}
