package com.sword.mylibrary.leetcode;

import com.sword.mylibrary.Asserts;

import java.util.Arrays;
import java.util.HashMap;

/**
 * <a href="https://leetcode.cn/problems/two-sum/">1. 两数之和</a>
 * <p>
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个 整数，并返回它们的数组下标。
 * <p>
 * 示例：nums = [2,7,11,15], target = 9
 * 输出：[0,1]
 * <p>
 * 思路：
 * 1.
 */
public class TwoSum {

    public static void main(String[] args) {
        int[] testData1 = {-1,-2,-3,-4,-5};
        System.out.println("result1: " + Arrays.toString(twoSum1(testData1, -8)));

        int[] testData2 = {3, 2, 4};
        System.out.println("result2: " + Arrays.toString(twoSum1(testData2, 6)));

        int[] testData3 = {3, 3};
        System.out.println("result3: " + Arrays.toString(twoSum1(testData3, 6)));
    }

    public static int[] twoSum1(int[] nums, int target) {
        for (int first = 0; first < nums.length - 1; first++) {
            for (int second = first + 1; second < nums.length; second++) {
                int sum = nums[first] + nums[second];
                if (sum == target) {
                    return new int[]{first, second};
                }
            }
        }
        return new int[]{0, 0};
    }

    public static int[] twoSum2(int[] nums, int target) {
        HashMap<Integer, Integer> numsTable = new HashMap<>();
        for (int i=0; i<nums.length; i++) {
            if (numsTable.containsKey(target - nums[i])) {
                return new int[]{numsTable.get(target - nums[i]), i};
            }
            numsTable.put(nums[i], i);
        }
        return new int[]{0, 0};
    }
}
