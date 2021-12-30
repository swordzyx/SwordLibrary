package com.example.swordlibrary.java.javaapidemo;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchSample {
  public static void main(String[] args) {
    
  }
  
  private static final int N = 4;
  void sample1() throws InterruptedException {
    CountDownLatch startSignal = new CountDownLatch(1);
    CountDownLatch doneSignal = new CountDownLatch(N);
    
    for (int i=0; i<N; i++) {
      new Thread(new Worker(startSignal, doneSignal)).start();
    }
    
    doSomethingElse();
    startSignal.countDown();
    doSomethingElse();
    doneSignal.await();
  }
  
  private void doSomethingElse() {}
  
  static class Worker implements Runnable {
    CountDownLatch startSignal, doneSignal;
    Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
      this.startSignal = startSignal;
      this.doneSignal = doneSignal;
    }

    @Override
    public void run() {
      try {
        startSignal.await();
        doWork();
        doneSignal.countDown();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    void doWork() {
      
    }
  }
}
