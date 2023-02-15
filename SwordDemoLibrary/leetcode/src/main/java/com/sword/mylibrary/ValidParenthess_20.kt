package com.sword.mylibrary

import java.util.LinkedList
import java.util.Stack

/**
 * 20. 有效的括号 https://leetcode.cn/problems/valid-parentheses/
 */

fun main() {
    isValid2Test()
}


/**
 * 执行用时：132 ms
 * 内存消耗：32.7 MB
 */
fun isValid(s: String): Boolean {
    val stack = Stack<Char>()
    s.forEach {
        when (it) {
            '(', '[', '{' -> stack.push(it)
            ')' -> if (stack.isEmpty() || stack.pop() != '(') return false
            ']' -> if (stack.isEmpty() || stack.pop() != '[') return false
            '}' -> if (stack.isEmpty() || stack.pop() != '{') return false
        }
    }
    return stack.isEmpty()
}

fun isValid1Test() {
    val input1 = "()"
    assert(isValid(input1))
    val input2 = "()[]{}"
    assert(isValid(input2))
    val input3 = "(]"
    assert(!isValid(input3))
    val input4 = "]"
    assert(!isValid(input4))
}

/**
 * 执行用时：144 ms
 * 内存消耗：34.1 MB
 */
fun isValid2(s: String): Boolean {
    val stack = LinkedList<Char>()
    val map = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{'
    )

    if (!map.containsValue(s[0])) return false

    s.forEach {
        if (map.containsValue(it))
            stack.push(it)
        else if (stack.isEmpty() || stack.pop() != map[it])
            return false
    }
    return stack.isEmpty()
}

fun isValid2Test() {
    val input1 = "()"
    println(isValid2(input1))
    val input2 = "()[]{}"
    println(isValid2(input2))
    val input3 = "(]"
    println(isValid2(input3))
    val input4 = "]"
    println(isValid2(input4))
}