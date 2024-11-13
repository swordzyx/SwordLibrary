package com.sword.mylibrary

import com.sword.mylibrary.leetcode.CoinChange
import com.sword.mylibrary.sort.InsertionSort

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        //val array = arrayOf(7, 3, 5, 8, 6, 7, 4, 5)


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
        /*val insertionSort = InsertionSort<Int?>()
        insertionSort.sort(array.copyOf(array.size))
        println("-----------------------------------------------------")
        println(insertionSort)*/

        testCoinChange()

    }

    private fun testCoinChange() {
        val coinChan = CoinChange()
        println("----------------------- 动态规划 --------------------------------")
        /*println("测试1: " + coinChan.coinChange1(coinChan.coins1, coinChan.amount1))
        println("测试2: " + coinChan.coinChange1(coinChan.coins2, coinChan.amount2))
        println("测试3: " + coinChan.coinChange1(coinChan.coins3, coinChan.amount3))*/
        println("测试4: " + coinChan.coinChange1(coinChan.coins4, coinChan.amount4))
    }
}
