package com.example.threadandroid;


import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 1. 在线程中使用死循环让线程一直运行，while(true){...}
 * 2. 提供一个接口供外界设置任务，setTask(Runnable task)。再 run 中执行外界设置的 task。不过需要处理同步问题，防止在执行 task 的时候，task 被替换。
 * 3. 提供一个接口供外界停止线程
 * 4. 实际上，Android 中的 HandlerThread 其实是一个壳，Looper 负责循环
 */
public class CustomThread extends Thread {
    Looper looper = new Looper();

    @Override
    public void run() {
        looper.loop();
    }

    class Looper {
        private Runnable task;
        private AtomicBoolean quit = new AtomicBoolean(false);

        synchronized void setTask(Runnable task) {
            this.task = task;
        }

        void quit() {
            quit.set(true);
        }
        void loop() {
            while (!quit.get()) {
                synchronized (this) {
                    if (task != null) {
                        task.run();
                        task = null;
                    }
                }
            }
        }

    }
}


