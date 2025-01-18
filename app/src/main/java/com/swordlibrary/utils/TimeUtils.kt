package com.swordlibrary.utils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * 参考
 * - https://github.com/Blankj/AndroidUtilCode  ---  TimeUtils
 */
object TimeUtils {
    /**
     * timeString: 例如 "2024-11-11 17:52:24", "23:45"
     * pattern: 例如 "yyyy-MM-dd HH:mm:ss"、"HH:mm"
     */
    fun hhmmToTimeMillis(timeString: String): Long {
        //将时间字符串解析成 Date 对象
        val targetDate = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(timeString)

        val resultCalendar = Calendar.getInstance()
        targetDate?.let {
            val timeCalendar = Calendar.getInstance()
            timeCalendar.time = it
            resultCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
            resultCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
            resultCalendar.set(Calendar.SECOND, 0)
            resultCalendar.set(Calendar.MILLISECOND, 0)
        }
        return resultCalendar.timeInMillis
    }
}