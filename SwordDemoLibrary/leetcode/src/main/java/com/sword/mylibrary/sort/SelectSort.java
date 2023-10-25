package com.sword.mylibrary.sort;

/**
 * 时间复杂度：O(n^2)
 * 空间复杂度：O(1)
 * 不稳定
 * @param <T>
 */
public class SelectSort<T extends Comparable<T>> extends Sort<T> {
  @Override
  String getName() {
    return "选择排序";
  }

  @Override
  void sort() {
    for (int end = datas.length - 1; end > 0; end--) {
      int max = 0;
      for (int i = 1; i <= end; i++) {
        if (datas[i].compareTo(datas[max]) > 0) {
          max = i;
        }
      }

      if (max != end) {
        T temp = datas[max];
        datas[max] = datas[end];
        datas[end] = temp;
      }
    }
  }
}
