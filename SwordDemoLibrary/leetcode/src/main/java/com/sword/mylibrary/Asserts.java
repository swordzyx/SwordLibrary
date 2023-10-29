package com.sword.mylibrary;

public class Asserts {
    public static void assertTrue(boolean condition) {
        try {
             if (!condition) throw new Exception("测试未通过");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
