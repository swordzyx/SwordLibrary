package com.sword.mylibrary;

import com.sword.mylibrary.sort.BubbleSort;
import com.sword.mylibrary.sort.BubbleSort1;
import com.sword.mylibrary.sort.InsertionSort;
import com.sword.mylibrary.sort.SelectSort;
import com.sword.mylibrary.sort.Sort;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Integer[] array = {7, 3, 5, 8, 6, 7, 4, 5};

        /*BubbleSort<Integer> bubbleSort = new BubbleSort<>();
        Integer[] newArray = Arrays.copyOf(array, array.length);
        bubbleSort.sort(newArray);
        System.out.println("-----------------------------------------------------");
        System.out.println(bubbleSort);


        BubbleSort1<Integer> bubbleSort1 = new BubbleSort1<>();
        Integer[] newArray1 = Arrays.copyOf(array, array.length);
        bubbleSort1.sort(newArray1);
        System.out.println("-----------------------------------------------------");
        System.out.println(bubbleSort1);*/


        /*SelectSort<Integer> selectSort = new SelectSort<>();
        selectSort.sort(Arrays.copyOf(array, array.length));
        System.out.println("-----------------------------------------------------");
        System.out.println(selectSort);*/

        InsertionSort<Integer> insertionSort = new InsertionSort<>();
        insertionSort.sort(Arrays.copyOf(array, array.length));
        System.out.println("-----------------------------------------------------");
        System.out.println(insertionSort);
    }
}
