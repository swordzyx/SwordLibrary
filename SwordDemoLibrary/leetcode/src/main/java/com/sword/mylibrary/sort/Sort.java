package com.sword.mylibrary.sort;


import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.util.Arrays;

public abstract class Sort<T extends Comparable<T>>  {
    private long timeCost;
    private int compareCount;
    private int swapCount;
    
    private final DecimalFormat decimalFormat = new DecimalFormat("#.00");
    
    protected T[] datas;

    
    abstract String getName();
    
    public void sort(T[] datas) {
        this.datas = datas;
        long begin = System.currentTimeMillis();
        sort();
        timeCost = System.currentTimeMillis() - begin;
    }
    
    public int compare(int i1, int i2) {
        compareCount++;
        return datas[i1].compareTo(datas[i2]);
    }
    
    public void swap(int i1, int i2) {
        swapCount++;
        T temp = datas[i1];
        datas[i1] = datas[i2];
        datas[i2] = temp;
    }
    
    abstract void sort();
    @NonNull
    @Override
    public String toString() {
        StringBuilder printInfo = new StringBuilder(); 
        for (T t: datas) {
            printInfo.append(t).append(" ");
        }
        printInfo.append("\n")
            .append("【").append(getName()).append("】\n")
            .append("稳定性：").append(isStable()).append("】\n")
            .append("比较次数：").append(compareCount).append("\n")
            .append("交换次数：").append(swapCount).append("\n")
            .append(" 耗时：").append((timeCost / 1000)).append("s(").append(timeCost).append("ms)");
        return printInfo.toString();
    }
    
    private String formatNumber(int number) {
        if (number < 10000) return number+"";
        
        if (number < 100000000) return decimalFormat.format(number / 10000.0) + " 万";
        return decimalFormat.format(number / 100000000.0) + "亿";
    }

    private boolean isStable() {
        if (this instanceof SelectSort) {
            return false;
        }
        
        Student[] students = new Student[10];
        for (int i = 1; i<=10; i++) {
            students[i] = new Student(10, i * 10);
        }
        sort((T[]) students);
        for (int i=0; i<10; i++) {
            if (students[i].score != (i+1)*10) {
                return false;
            }
        }
        return true;
    }
}
