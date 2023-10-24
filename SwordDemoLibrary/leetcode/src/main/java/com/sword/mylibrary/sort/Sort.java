package com.sword.mylibrary.sort;


import androidx.annotation.NonNull;

import java.util.Arrays;

public abstract class Sort<T>  {
    private long timeCost;
    protected T[] datas;

    
    abstract String getName();
    
    public void sort(T[] datas) {
        this.datas = datas;
        long begin = System.currentTimeMillis();
        sort();
        timeCost = System.currentTimeMillis() - begin;
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
            .append(getName())
            .append(" 耗时：")
            .append((timeCost / 1000))
            .append("s(").append(timeCost).append("ms)");
        return printInfo.toString();
    }
}
