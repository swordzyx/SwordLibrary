package com.sword.mylibrary.sort;

public class BubbleSort<T extends Comparable<T>> extends Sort<T> {

    @Override
    String getName() {
        return "冒泡排序";
    }

    @Override
    void sort() {
        for (int end = datas.length - 1; end > 0; end--) {
            for (int start = 0; start < end; start++) {
                int i = start + 1;
                if (datas[start].compareTo(datas[i]) > 0) {
                    T temp = datas[start];
                    datas[start] = datas[i];
                    datas[i] = temp;
                }
            }
        }
    }
}
