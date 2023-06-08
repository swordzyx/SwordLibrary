package sword.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 扔物线课程 - 多线程和线程同步课程代码
 */
public class ThreadSample {

  public static void main(String[] args) {
    callable();
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
    //创建一个 Callable
    Callable<String> callable = () -> {
      try {
        Thread.sleep(1500);
      } catch (InterruptedException ignored){}
      return "Callable Done~~~";
    };
    //执行这个 Callable
    ExecutorService executorService = Executors.newCachedThreadPool();
    Future<String> callableFuture = executorService.submit(callable);

    //获取并打印 Callable 的执行结果
    while(true) {
      if (callableFuture.isDone()) {
        try {
          System.out.println(callableFuture.get());
        } catch (ExecutionException | InterruptedException e) {
          e.printStackTrace();
        }
        break;
      }
    }


  }
}
