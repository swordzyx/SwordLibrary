package com.sword.mylibrary.sort;

public class BubbleSort1<T extends Comparable<T>> extends Sort<T> {

    @Override
    String getName() {
        return "冒泡排序优化";
    }

    @Override
    void sort() {
        //7, 3, 5, 8, 6, 7, 4, 5
        for (int end = datas.length - 1; end > 0; end--) {
            int lastSwapIndex = 0;
            for (int start = 0; start < end; start++) {
                int i = start + 1;
                if (datas[start].compareTo(datas[i]) > 0) {
                    T temp = datas[start];
                    datas[start] = datas[i];
                    datas[i] = temp;
                    lastSwapIndex = i;
                }
            }
            //有序序列的第一个元素的索引，如果是向前交换，有序序列的索引就是比较元素的索引；如果是向后交换，有序序列的索引就是被交换的元素的索引
            end = lastSwapIndex;
        }
    }
}
