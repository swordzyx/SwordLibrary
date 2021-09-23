package com.exampl

import java.util.*
import java.util.regex.Pattern

fun main() {
    val idNumber = "43042419960801726"
    computeVerifyCode(idNumber)
}

/**
 * 检查是否为有效身份证号
 */
fun isValidCard(idCard: String): Boolean {
    return when (idCard.length) {
        18 -> isValidCard18(idCard)
        15 -> isValidCard15(idCard)
        10 -> {
            val cardval = isValidCard10(idCard)
            cardval?.get(2) == "true"
        }
        else -> false
    }
}


/**
 * 解析台湾，澳门，香港身份证相关信息，校验是否为台湾，澳门或香港的身份证
 * @param idCard: 身份证号
 * @return Array<String>: 身份证信息数组
 *      [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false) 若不是身份证件号码则返回null
 */
@Suppress("UNCHECKED_CAST")
private fun isValidCard10(idCard: String): Array<String>? {
    if (idCard.trim().isEmpty()) {
        return null
    }
    val info = arrayOfNulls<String>(3) as Array<String>
    val card = idCard.replace("[()]", "")
    if (card.length != 8 && card.length != 9 && idCard.length != 10) {
        return null
    }
    // 台湾
    if (idCard.matches(Regex("^[a-zA-Z][0-9]{9}$"))) {
        info[0] = "台湾"
        when (idCard.substring(1, 2)) {
            "1" -> info[1] = "M"
            "2" -> info[1] = "F"
            else -> {
                info[1] = "N"
                info[2] = "false"
                return info
            }
        }
        info[2] = if (isValidTWCard(idCard)) "true" else "false"
    } else if (idCard.matches(Regex("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$"))) {
        // 香港
        info[0] = "香港"
        info[1] = "N"
        info[2] = if (isValidHKCard(idCard)) "true" else "false"
    } else {
        return null
    }
    return info
}


val hkFirstCode = hashMapOf(
    "A" to 1, // 持证人拥有香港居留权
    "B" to 2, // 持证人所报称的出生日期或地点自首次登记以后，曾作出更改
    "C" to 3, // 持证人登记领证时在香港的居留受到入境事务处处长的限制
    "N" to 14, // 持证人所报的姓名自首次登记以后，曾作出更改
    "O" to 15, // 持证人报称在香港、澳门及中国以外其他地区或国家出生
    "R" to 18, // 持证人拥有香港入境权
    "U" to 21, // 持证人登记领证时在香港的居留不受入境事务处处长的限制
    "W" to 23, // 持证人报称在澳门地区出生
    "X" to 24, // 持证人报称在中国大陆出生
    "Z" to 26, // 持证人报称在香港出生
)
/**
 * 验证香港身份证号码
 * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
 * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
 * @param idCard 身份证号码
 * @return 验证码是否符合
 */
private fun isValidHKCard(idCard: String): Boolean {
    var card = idCard.replace("[()]", "")
    var sum: Int
    if (card.length == 9) {
        sum =
            (Character.toUpperCase(card[0]) - 55).code * 9 + (Character.toUpperCase(card[1]) - 55).code * 8;
        card = card.substring(1, 9)
    } else {
        sum = 522 + (Character.toUpperCase(card[0]) - 55).code * 8
    }
    val start = idCard.substring(0, 1)
    hkFirstCode[start] ?: return false

    val mid = card.substring(1, 7)
    val end = card.substring(7, 8)
    val chars = mid.toCharArray()
    var iflag = 7
    for (c in chars) {
        sum += c.code * iflag
        iflag--
    }
    sum += if ("A" == end.uppercase()) {
        10
    } else {
        Integer.parseInt(end)
    }
    return sum % 11 == 0
}


private val twFirstCode = hashMapOf(
    "A" to 10,
    "B" to 11,
    "C" to 12,
    "D" to 13,
    "E" to 14,
    "F" to 15,
    "G" to 16,
    "H" to 17,
    "J" to 18,
    "K" to 19,
    "L" to 20,
    "M" to 21,
    "N" to 22,
    "P" to 23,
    "Q" to 24,
    "R" to 25,
    "S" to 26,
    "T" to 27,
    "U" to 28,
    "V" to 29,
    "X" to 30,
    "Y" to 31,
    "W" to 32,
    "Z" to 33,
    "I" to 34,
    "O" to 35
)
/**
 * 验证台湾身份证号码
 */
private fun isValidTWCard(idCard: String): Boolean {
    if (idCard.trim().isEmpty()) {
        return false;
    }
    val start = idCard.substring(0, 1)
    val iStart = twFirstCode[start] ?: return false

    var sum = iStart / 10 + (iStart % 10) * 9
    val chars = idCard.substring(1, 9).toCharArray()
    var iflag = 8
    for (c in chars) {
        sum += c.digitToInt() * iflag
        iflag--
    }
    return (if (sum % 10 == 0) 0 else (10 - sum % 10)) == idCard.substring(9, 10).toInt()
}


private const val CHINA_ID_MIN_LENGTH = 15
private val cityCodes = hashMapOf(
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
/**
 * 验证 15 位的大陆身份证号是否合法
 * 1. 校验是否为数字
 * 2. 校验 0，1 位是否为省份的代码
 * 3. 校验 6 - 12 位是否位正确的生日格式
 */
fun isValidCard15(idCard: String): Boolean {
    if (CHINA_ID_MIN_LENGTH != idCard.length) {
        return false
    }
    //校验身份证是否全部为数字
    if (idCard.matches(Regex(NUMBER_REGEX))) {
        // 校验省份
        val proCode = idCard.substring(0, 2)
        if (null == cityCodes[proCode]) {
            return false
        }

        //校验生日（两位年份，补充为19XX）
        return isBirthday("19" + idCard.substring(6, 12))
    } else {
        return false;
    }
}



private const val NUMBER_REGEX = "\\d+"
private const val CHINA_ID_MAX_LENGTH = 18
/**
 * 验证 18 位的大陆身份证号是否合法
 * 1. 校验 6 - 14 位是否位正确的生日格式
 * 2. 校验最后一位校验码是否正确，通过前 17 位身份证号计算出第 18 位，并与 $idCard 中第 18 位作比较
 */
private fun isValidCard18(idCard: String): Boolean {
    if (CHINA_ID_MAX_LENGTH != idCard.length) {
        return false
    }
    //校验生日
    if (!isBirthday(idCard.substring(6, 14))) {
        return false
    }

    // 前17位
    val code17 = idCard.substring(0, 17)
    // 第18位
    val code18 = Character.toLowerCase(idCard[17])
    if (code17.matches(Regex(NUMBER_REGEX))) {
        // 获取校验位，检查校验位是否正确
        val checkCode = getVerifyCode18(code17)
        return checkCode == code18
    }
    return false
}

/**
 * Y - 校验码：0 - 1, 1 - 0, 2 - X, 3 - 9, 4 - 8, 5 - 7, 6 - 6, 7 - 5, 8 - 4, 9 - 3, 10 - 2
 */
val yToVerifyCode = arrayOf('1', '0', 'X', '9', '7', '6', '5', '4', '3', '2')
val wis = arrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
private const val regexNumber = "^[0-9]*$"
fun getVerifyCode18(idNumber: String): Char? {
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


private const val BIRTHDAY = "^(\\d{2,4})([/\\-.年]?)(\\d{1,2})([/\\-.月]?)(\\d{1,2})日?$"
private fun isBirthday(value: String): Boolean {
    val matcher = Pattern.compile(BIRTHDAY).matcher(value)
    if (matcher.find()) {
        val year = Integer.parseInt(matcher.group(1)!!)
        val month = Integer.parseInt(matcher.group(3)!!)
        val day = Integer.parseInt(matcher.group(5)!!)
        return isBirthday(year, month, day)
    }
    return false
}

/**
 * 校验日期的年，月，日是否有效
 */
private fun isBirthday(year: Int, month: Int, day: Int): Boolean {
    // 验证年
    val thisYear = year(Date())
    if (year < 1900 || year > thisYear) {
        return false
    }

    // 验证月
    if (month < 1 || month > 12) {
        return false;
    }
    
    // 验证日
    if (day < 1 || day > 31) {
        return false;
    }
    // 检查几个特殊月的最大天数
    if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
        return false;
    }
    if (month == 2) {
        // 在2月，非闰年最大28，闰年最大29
        return day < 29 || (day == 29 && isLeapYear(year))
    }
    return true;
}


/**
 * 判断 {#year} 是否为闰年 
 */
fun isLeapYear(year: Int): Boolean {
    return GregorianCalendar().isLeapYear(year)
}

/**
 * 获取 date 所在的年份（例如 1900，2000 等）
 */
fun year(date: Date): Int {
    val d = Calendar.getInstance()
    d.time = date
    return d.get(Calendar.YEAR)
}

/**
 * 将 15 位的身份证号转为 18 位，在第 6 位（从 0 开始计数）插入 19
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
    id += getVerifyCode18(id)
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
 * 计算身份证的最后一位对应的校验码
 * @param idNumber：身份证前 16 位
 */
//S = A2*W2 + A3*W3 + ... + A18*W18
//Wi = 2^(i-1) mod 11，Wi 其实是固定值
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






