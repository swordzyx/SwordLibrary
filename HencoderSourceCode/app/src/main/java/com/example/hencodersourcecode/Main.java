package com.example.hencodersourcecode;

public class Main {
    public static void main(String[] args) {
        thread();
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


}
