package com.sword.mylibrary.sort;

public class BubbleSort<T> extends Sort<T> {

    @Override
    void sort() {
        for (int end = datas.length - 1; end > 0; end--) {
            for (int start = 0; start < end; start++) {
                int i = start + 1;
                if (datas[start] > datas[i]) {
                    int temp = datas[start];
                    datas[start] = datas[i];
                    datas[i] = temp;
                }
            }
        }
    }
}
