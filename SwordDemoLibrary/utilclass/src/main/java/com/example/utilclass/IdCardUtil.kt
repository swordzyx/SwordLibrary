package com.example.utilclass

import java.util.regex.Matcher
import java.util.regex.Pattern

fun main() {
    val idNumber = "43042419960801726"
    computeVerifyCode(idNumber)
}

/**
 * 大陆身份证号验证
 */
fun checkIdNumberRegex(idNumber: String): Boolean {
    return Pattern.matches("^([0-9]{17}[0-9Xx])|([0-9]{15})$", idNumber)
}

/** 香港地域编码值 **/
private const val HONGKONG_AREACODE = 810000

/** 澳门地域编码值 **/
private const val MACAO_AREACODE = 820000

/** 台湾地域编码值 **/
private const val TAIWAN_AREACODE = 710000

/** 大陆地区地域编码最大值 **/
private const val MAX_MAINLAND_AREACODE = 659004

/** 大陆地区地域编码最小值 **/
private const val MIN_MAINLAND_AREACODE = 110000

/**
 * 身份地区码校验
 */
fun checkIdNumberArea(idNumberArea: String): Boolean {
    val areaCode = Integer.parseInt(idNumberArea);
    if (areaCode == HONGKONG_AREACODE || areaCode == MACAO_AREACODE || areaCode == TAIWAN_AREACODE) {
        return true;
    }
    if (areaCode in MIN_MAINLAND_AREACODE..MAX_MAINLAND_AREACODE) {
        return true;
    }
    return false;
}

/**
 * 将 15 位地身份证号转为 18 位
 */
fun convertFifteenToEighteen(idNumber: String): String {
    if (15 != idNumber.length) {
        return idNumber
    }
    var id: String = idNumber.substring(0, 6) + "19" + idNumber.substring(6, 15)
    id += getVerifyCode(id)
    return id;
}


/**
 * Y - 校验码：0 - 1, 1 - 0, 2 - X, 3 - 9, 4 - 8, 5 - 7, 6 - 6, 7 - 5, 8 - 4, 9 - 3, 10 - 2
 */
val yToVerifyCode = arrayOf("1", "0", "X", "9", "7", "6", "5", "4", "3", "2")
val wis = arrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
private const val regexNumber = "^[0-9]*$"
fun getVerifyCode(idNumber: String): String? {
    if (!Pattern.matches(regexNumber, idNumber.substring(0, 17))) {
        return null
    }

    var s = 0
    for (i in 0..17) {
        val ai = idNumber[i].digitToInt()
        val wi = wis[i]
        s += (ai * wi)
    }
    return yToVerifyCode[s % 11]
}


/**
 * 计算身份证的最后一位对应的校验码
 * @param idNumber：身份证前 16 位
 */
//S = A2*W2 + A3*W3 + ... + A18*W18
//Wi = 2^(i-1) mod 11
fun computeVerifyCode(idNumber: String) {
    var s: Double = 0.0
    val chars = idNumber.toCharArray()
    //i 的取值范围：0~16
    for ((i, c) in chars.withIndex()) {
        val ai = c.digitToInt()
        val wi = Math.pow(2.0, 18.0 - i - 1) % 11
        println("i: $i, ai: $ai, wi: $wi")
        s += (ai * wi)
    }
    val y = s % 11
    println("y: $y")
}



