package sword.kotlin

import kotlin.system.exitProcess
import kotlin.text.StringBuilder

/**
 * Kotlin 编程第一课 - 04｜实战：构建一个Kotlin版本的四则运算计算器
 */

fun main() {

}

class Caculator {
    fun start() {
        val helpInfo = """
        ---------------------------------------
        请输入标准的算式，并且按回车；
        比如：1 + 1，注意符合与数字之间要有空格。
        输入exit，退出程序。
        ----------------------------------------""".trimIndent()
        println(helpInfo)
        while (true) {
            val input: String = readlnOrNull() ?: continue

            if (shouldExit(input)) exitProcess(0)

            val result = caculate(input)
            if (result == null) {
                println("表达式格式错误，计算失败")
            } else {
                println("计算结果为：$result")
            }
        }
    }

    fun shouldExit(input: String): Boolean {
        return input == "exit"
    }

    fun caculate(expressionString: String): String? {
        val expression = Expression.parseExpression(expressionString)
        if (expression == null) {
            println("表达式格式错误")
            return null
        }

        return when (expression.operator) {
            Operator.ADD -> add(expression.left, expression.right)
            Operator.MINUS -> minus(expression.left, expression.right)
            Operator.MULTI -> multi(expression.left, expression.right)
            Operator.DIVI -> divide(expression.left, expression.right)
        }
    }

    private fun add(left: String, right: String): String {
        val result = StringBuilder()
        var leftIndex = left.length - 1
        var rightIndex = right.length - 1
        //往前进的数值
        var carry = 0
        while (leftIndex >= 0 || rightIndex >= 0) {
            val leftDigit = left.getOrNull(leftIndex)?.digitToInt() ?: 0
            val rightDigit = right.getOrNull(rightIndex)?.digitToInt() ?: 0
            val digitSum = leftDigit + rightDigit + carry
            carry = digitSum / 10
            result.append(digitSum % 10)

            --leftIndex
            --rightIndex
        }

        if (carry != 0) {
            result.append(carry)
        }
        return result.reverse().toString()
    }

    private fun minus(left: String, right: String): String {
        val result = StringBuilder()
        //减数
        val subtrahend: String
        //被减数
        val minuend: String
        //比较 left 和 right 的大小
        var compareResult = compare(left, right)
        if (compareResult > 0) {
            subtrahend = left
            minuend = right
        } else if (compareResult < 0) {
            subtrahend = right
            minuend = left
        } else {
            return "0"
        }

        var subtrahendIndex = subtrahend.length - 1
        var minuendIndex = minuend.length - 1
        var carry = 0
        while (subtrahendIndex >= 0 || minuendIndex >= 0) {
            val subtrahendDigit = subtrahend.getOrNull(subtrahendIndex)?.digitToInt() ?: 0
            val minuendDigit = minuend.getOrNull(minuendIndex)?.digitToInt() ?: 0

            carry = if (subtrahendDigit < (minuendDigit + carry)) {
                result.append((subtrahendDigit + 10) - minuendDigit - carry)
                1
            } else {
                result.append(subtrahendDigit - minuendDigit - carry)
                0
            }
            --subtrahendIndex
            --minuendIndex
        }

        if (compareResult < 0) result.append("-")

        return result.reverse().toString()
    }

    private fun compare(left: String, right: String): Int {
        if (left.length > right.length) {
            return 1
        } else if (left.length < right.length) {
            return -1
        } else {
            left.forEachIndexed { index, digitChar ->
                if (digitChar > right[index]) {
                    return 1
                } else if (digitChar < right[index]) {
                    return -1
                }
            }
            return 0
        }
    }

    private fun multi(left: String, right: String): String {
        return (left.toInt() * right.toInt()).toString()
    }

    private fun divide(left: String, right: String): String {
        return (left.toDouble() / right.toDouble()).toString()
    }
}

data class Expression(val left: String, val operator: Operator, val right: String) {
    companion object {
        fun parseExpression(expression: String): Expression? {
            var operator: Operator? = null
            var expressionNumbers: List<String>? = null

            Operator.values().forEach {
                if (expression.contains(it.value)) {
                    operator = it
                    expressionNumbers = expression
                        .replace(" ", "")
                        .also { expressionWithTrim ->
                            println("消除空格后的表达式：$expressionWithTrim")
                        }.split(it.value)
                }
            }

            if (expressionNumbers == null || expressionNumbers!!.size != 2) {
                println("表达式解析失败")
                return null
            }

            return try {
                Expression(
                    expressionNumbers!![0],
                    operator!!,
                    expressionNumbers!![1]
                ).also {
                    println("表达式：$it")
                }
            } catch (e: NumberFormatException) {
                println(e.message)
                null
            }
        }
    }
}

enum class Operator(val value: String) {
    ADD("+"),
    MINUS("-"),
    MULTI("*"),
    DIVI("/")
}