package com.swordlibrary

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 参考：
 * - https://github.com/Blankj/AndroidUtilCode  ---  LogUtils
 * - https://devv.ai/search?threadId=e3hss7be0nb4
 * - B3: LogUtils.java
 */
object LogUtils {
    var publicTag = "SwordLibrary"
    private val isDebug = true

    private const val LOG_FILE_PREFIX = "log_"
    private const val LOG_FILE_SUFFIX = ".log"
    private const val MAX_FILE_SIZE = 10 * 1024 * 1024 // 10MB

    private var logFileDirPath: String? = null

    fun init(context: Context) {
        logFileDirPath = "${context.filesDir.absolutePath}/logs"
    }

    @SuppressLint("ConstantLocale")
    private val fileNameDateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

    private data class LogMessage(
        val priority: Int,
        val head: String,
        val msg: String,
        val timestamp: Long = System.currentTimeMillis()
    )

    fun d(msg: String) {
        log(Log.DEBUG, "", msg)
    }

    fun d(head: String, msg: String) {
        log(Log.DEBUG, head, msg)
    }

    fun w(msg: String) {
        log(Log.WARN, "", msg)
    }

    fun w(head: String, msg: String) {
        log(Log.WARN, head, msg)
    }

    fun e(msg: String) {
        log(Log.ERROR, "", msg)
    }

    fun e(head: String, msg: String) {
        log(Log.ERROR, head, msg)
    }

    private fun log(priority: Int, head: String, msg: String) {
        val logContent = if (head.isEmpty()) "" else "[$head] $msg"
        when(priority) {
            Log.VERBOSE -> if(isDebug) Log.v(publicTag, logContent)
            Log.DEBUG -> if (isDebug) Log.d(publicTag, logContent)
            Log.INFO -> if (isDebug) {
                Log.i(publicTag, logContent)
            }
            Log.WARN -> {
                Log.w(publicTag, logContent)
                writeToFile(priority, head, msg)
            }
            Log.ERROR -> {
                Log.e(publicTag, logContent)
            }
        }

        if (isDebug || priority == Log.WARN || priority == Log.ERROR) {
            Log.d(publicTag, "write $priority - $head - $msg to file")
            writeToFile(priority, head, msg)
        }
    }

    private fun writeToFile(priority: Int, head: String, msg: String) {
        val logMessage = LogMessage(priority, head, msg)

        //创建日志文件
        logFileDirPath?.let {
            val logFile = File(it, LOG_FILE_PREFIX + fileNameDateFormat.format(Date()) + LOG_FILE_SUFFIX).also {
                d("log file path: ${it.absolutePath}")
            }
        }

        //将日志内容写入文件
    }
}