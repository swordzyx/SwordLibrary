package com.example.hencodersourcecode;

import java.sql.SQLOutput;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
//        thread();
        threadFactory();
    }

    //使用 Thread 定义线程
    static void thread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("Thread Started");
            }
        };
        thread.start();
    }

    //使用 Runnable 定义线程
    static void runnable() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread with Runnable Start");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    //线程工厂
    static void threadFactory() {
        ThreadFactory factory = new ThreadFactory() {
            int count = 0;
            @Override
            public Thread newThread(Runnable r) {
                count ++;
                return new Thread(r, count + " started");
            }
        };

        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " Started !");
            }
        };

        for (int i=0; i<1000; i++) {
            factory.newThread(r).start();
        }
    }

    static void executor() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread with Runnable started");
            }
        };

        Executor executor = Executors.newCachedThreadPool();
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);

        ExecutorService myExecutor = new ThreadPoolExecutor(5, 100, 5, TimeUnit.MINUTES, new SynchronousQueue<Runnable>());
        myExecutor.execute(runnable);
    }

}
