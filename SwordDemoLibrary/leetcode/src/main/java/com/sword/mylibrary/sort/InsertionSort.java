package com.sword.mylibrary.sort;

/**
 * 最好时间复杂度：O(n)
 * 最坏时间复杂度：O(n^2)
 * 平均复杂度：O(n^2)
 *
 * @param <T>
 */
public class InsertionSort<T extends Comparable<T>> extends Sort<T> {
    @Override
    String getName() {
        return "插入排序";
    }

    @Override
    void sort() {
        for (int i = 1; i < datas.length; i++) {
            int currentIndex = i;
            T currData = datas[i];
            //用 while 循环可以减少遍历的次数，提前结束循环
            while (currentIndex > 0 && compare(currData, datas[currentIndex - 1]) < 0) {
                datas[currentIndex] = datas[currentIndex - 1];
                currentIndex--;
            }
            datas[currentIndex] = currData;
        }
    }

}
