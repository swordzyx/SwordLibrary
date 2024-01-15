package com.sword.kotlin

import sword.kotlin.Caculator
import sword.utils.FLOAT_EPSILON
import kotlin.test.Test
import kotlin.test.assertEquals

class TestCaculatorSample {

    @Test
    fun testCaculate() {
        val caculator = Caculator()
        var result = caculator.caculate("1+2")
        assertEquals("3", result)

        result = caculator.caculate("1+2333333333333332")
        assertEquals("2333333333333333", result)

        result = caculator.caculate("1-2333333333333332")
        assertEquals("-2333333333333331", result)

        result = caculator.caculate("2333333333333332 - 3")
        assertEquals("2333333333333329", result)

        //用例：-91-90

        //用例：91-98
    }
    
    @Test
    fun utilTest() {
        println("FLOAT_EPSILON: $FLOAT_EPSILON")
    }

}