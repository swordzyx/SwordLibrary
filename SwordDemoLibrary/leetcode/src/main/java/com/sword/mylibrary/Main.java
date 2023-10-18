package com.sword.mylibrary;

import com.sword.mylibrary.sort.BubbleSort;
import com.sword.mylibrary.sort.Sort;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] array = {7, 3, 5, 8, 6, 7, 4, 5};

        BubbleSort bubbleSort = BubbleSort();
        int[] newArray = Arrays.copyOf(array, array.length);


    }
}
