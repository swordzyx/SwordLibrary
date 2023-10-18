package com.sword.mylibrary.sort;


import androidx.annotation.NonNull;

import java.util.Arrays;

public abstract class Sort<T>  {
    private long timeCost;
    protected T[] datas;

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
        String timeStr = "耗时：" + (timeCost / 1000) + "s(" + timeCost + "ms)";
        return timeStr;
    }
}
