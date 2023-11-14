package com.sword.mylibrary.leetcode;

import com.sword.mylibrary.Asserts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * <a href="https://leetcode.cn/problems/longest-substring-without-repeating-characters/">无重复字符的最长子串</a>
 * <p> 
 * 输入: s = "abcabcbb"
 * 输出: 3 
 */
public class LengthOfLongestSubstring {
  public static void main(String[] args) {
    LengthOfLongestSubstring lengthOfLongestSubstring = new LengthOfLongestSubstring();
    
    String str = "abcabcbb";
    System.out.println(str + "\n最长子串长度：" + lengthOfLongestSubstring.solution2(str));

    str = "bbbbb";
    System.out.println(str + "\n最长子串长度：" + lengthOfLongestSubstring.solution2(str));

    String str3 = "abba";
    System.out.println(str3 + "\n最长子串长度：" + lengthOfLongestSubstring.solution2(str3));
  }

  /**
   * 解法1：滚动窗口，使用 HashMap 可以快速将做指针移动到重复字符串的位置，而不必逐个遍历
   * 1. 使用 HashMap 保存遍历过的字符，空间复杂度为字符串中不重复的字符的数量
   * 2. 先移动右指针，遇到重复的字符或者到达边界，再移动左指针
   * </p>
   * 5ms 击败 64.82%使用 Java 的用户
   * 41.45MB 击败 58.95%使用 Java 的用户
   */
  public int solution1(String str) {
    if (str.length() == 0) return 0;
    
    int result = 0;
    int start = 0;
    int end;
    
    HashMap<Character, Integer> tempContainer = new HashMap<>();
    tempContainer.put(str.charAt(start), start);
    
    for (end = 1; end < str.length(); end++) {
      char c = str.charAt(end);
      if (tempContainer.containsKey(c) && tempContainer.get(c) >= start) {
        result = Math.max(result, end - start);
        System.out.println("debug result: " + result + ", start: " + start + 
            ", end: " + end + ", repeat index: " + tempContainer.get(c) + ", repeat char: " + c);
        start = tempContainer.get(c) + 1;
      }
      tempContainer.put(str.charAt(end), end);
    }
    result = Math.max(result, end - start);
    System.out.println("debug result after for: " + result);
    return result;
  }

  /**
   * 官方题解：
   * 1. 使用 HashSet 保存已遍历的字符
   * 2. 先移动右指针，遇到重复的字符或者到达边界，再移动左指针
   * <p> 
   * 129ms 击败 6.00%使用 Java 的用户
   * 42.18MB 击败 8.50%使用 Java 的用户
   */
  public int solution2(String str) {
    int length = str.length();
    if (length == 0) return 0;
    
    int result = 0;
    int right = 0;
    
    HashSet<Character> container = new HashSet<>();
    for (int i = 0; i < str.length(); i++) {
      if (i != 0) {
        container.remove(str.charAt(i - 1));
      }
      
      while (right < length && !container.contains(str.charAt(right))) {
        container.add(str.charAt(right));
        ++right;
      }
      
      result = Math.max(result, right - i);
      System.out.println("result: " + result + ", right: " + right + ", i: " + i);
    }
    return result;
  }

}
