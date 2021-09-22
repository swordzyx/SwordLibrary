package com.example.utilclass

import java.lang.IllegalArgumentException
import java.util.regex.Pattern

fun main() {
    val idNumber = "43042419960801726"
    computeVerifyCode(idNumber)
}

/**
 * 检查是否为有效身份证号
 */
fun isValieCard(idCard: String): Boolean {
    return when(idCard.length) {
        18 -> isValidCard18(idCard)
        15 -> isValidCard15(idCard)
        10 -> {
            val cardval = isValidCard10(idCard)
            cardval[2]=="true"
        }
        else -> false
    }
}

fun isValidCard10(idCard: String): Array<String> {
    TODO()
}


private const val CHINA_ID_MIN_LENGTH = 15
val cityCodes = hashMapOf(
    "11" to "北京",
    "12" to "天津",
    "13" to "河北",
    "14" to "山西",
    "15" to "内蒙古",
    "21" to "辽宁",
    "22" to "吉林",
    "23" to "黑龙江",
    "31" to "上海",
    "32" to "江苏",
    "33" to "浙江",
    "34" to "安徽",
    "35" to "福建",
    "36" to "江西",
    "37" to "山东",
    "41" to "河南",
    "42" to "湖北",
    "43" to "湖南",
    "44" to "广东",
    "45" to "广西",
    "46" to "海南",
    "50" to "重庆",
    "51" to "四川",
    "52" to "贵州",
    "53" to "云南",
    "54" to "西藏",
    "61" to "陕西",
    "62" to "甘肃",
    "63" to "青海",
    "64" to "宁夏",
    "65" to "新疆",
    "71" to "台湾",
    "81" to "香港",
    "82" to "澳门",
    "91" to "国外"
)
private fun isValidCard15(idCard: String): Boolean {
    if (CHINA_ID_MIN_LENGTH != idCard.length) {
        return false
    }
    if (idCard.matches(Regex(NUMBER_REGEX))) {
        // 省份
        val proCode = idCard.substring(0, 2)
        if (null == cityCodes.get(proCode)) {
            return false
        }

        //校验生日（两位年份，补充为19XX）
        return false != isBirthday("19" + idCard.substring(6, 12));
    } else {
        return false;
    }
}

private const val NUMBER_REGEX = "\\d+"
private const val CHINA_ID_MAX_LENGTH = 18
private fun isValidCard18(idCard: String): Boolean {
    if (CHINA_ID_MAX_LENGTH != idCard.length) {
        return false
    }

    //校验生日
    if (false == isBirthday(idCard.substring(6, 14))) {
        return false
    }

    // 前17位
    val code17 = idCard.substring(0, 17)
    // 第18位
    val code18 = Character.toLowerCase(idCard[17])
    if (code17.matches(Regex(NUMBER_REGEX))) {
        // 获取校验位，检查校验位是否正确
        val checkCode = getCheckCode18(code17);
        return checkCode == code18
    }
    return false
}

fun getCheckCode18(code17: String): Any {
    TODO("Not yet implemented")
}

fun isBirthday(substring: String): Any? {
    TODO("Not yet implemented")
}

/**
 * 将 15 位的身份证号转为 18 位
 */

@Throws(IllegalArgumentException::class)
fun convertFifteenToEighteen(idNumber: String): String {
    if (!idNumber.matches(Regex(NUMBER_REGEX))) {
        throw IllegalArgumentException("身份证格式不合法")
    }

    if (15 != idNumber.length) {
        return idNumber
    }
    var id: String = idNumber.substring(0, 6) + "19" + idNumber.substring(6, 15)
    id += getVerifyCode(id)
    return id
}


/**
 * 大陆身份证号验证
 * 对于 18 位身份证：校验前 17 位是否位数字，最后一位是否位数字或者大小写 X
 * 对于 15 位身份证：校验是否位数字
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
 * 身份地区码校验，身份证的 0-5 位
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






