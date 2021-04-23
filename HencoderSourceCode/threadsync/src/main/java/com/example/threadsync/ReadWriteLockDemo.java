package com.example.threadsync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo implements TestDemo  {
    private int x = 0;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();

    public void test() {
        Lock lock1 = new ReentrantLock();
        lock1.lock();
        try {
            x++;
        } finally {
            lock1.unlock();
        }
    }


    private void count() {
        writeLock.lock();
        try {
            x++;
        } finally {
            writeLock.unlock();
        }
    }

    private void print(int time) {
        readLock.lock();
        try {
            System.out.print(x + " ");
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void runTest() {
    }
}
