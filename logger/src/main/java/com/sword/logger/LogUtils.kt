package com.sword.logger

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.LinkedBlockingQueue

/**
 * 参考：
 * - https://github.com/Blankj/AndroidUtilCode  ---  LogUtils
 */
object LogUtils {
    private var publicTag = "SwordLibrary"
    private val tag = "Log"
    private val isDebug = true
    private var logToFile = false

    private const val LOG_FILE_PREFIX = "log_"
    private const val LOG_FILE_SUFFIX = ".log"
    private const val MAX_FILE_SIZE = 10 * 1024 * 1024 // 10MB
    private const val BUFFER_SIZE = 8192 // 8KB
    private const val MAX_BATCH_SIZE = 100 //最大批量写入日志条数
    private const val WRITE_PERIOD: Long = 1000 * 5 // 5秒写一次日志

    private var logFileDirPath: String? = null
    private var coroutineScope: CoroutineScope? = null

    @SuppressLint("ConstantLocale")
    private val fileNameDateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    private val logDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    private val contentString = StringBuilder(BUFFER_SIZE)

    /**
     * 日志缓冲区
     * LinkedBlockingQueue 是一个线程安全的阻塞队列，当队列为空时，从队列中获取元素的操作会被阻塞
     */
    private var logQueue: LinkedBlockingQueue<LogMessage>? = null


    private data class LogMessage(
        val priority: Int,
        val head: String,
        val msg: String,
        val timestamp: String = logDateFormat.format(Date()),
    )
    
    fun init(application: Application) {
        logFileDirPath = "${application.filesDir.absolutePath}/logs"

        application.registerActivityLifecycleCallbacks(
            AppLifecycleObserver(
                onAppCreate = null,
                onAppDestory = {
                    release()
                })
        )
    }

    fun openFileLog() {
        if (!logToFile) {
            logToFile = true
            initFileLog()
        }
    }

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
        val logContent = if (head.isEmpty()) msg else "[$head] $msg"
        when (priority) {
            Log.VERBOSE -> if (isDebug) Log.v(publicTag, logContent)
            Log.DEBUG -> if (isDebug) Log.d(publicTag, logContent)
            Log.INFO -> if (isDebug) {
                Log.i(publicTag, logContent)
            }

            Log.WARN -> {
                Log.w(publicTag, logContent)
            }

            Log.ERROR -> {
                Log.e(publicTag, logContent)
            }
        }

        if (!logToFile || logFileDirPath.isNullOrEmpty()) {
            return
        }

        if (priority == Log.WARN || priority == Log.ERROR) {
            logQueue?.let {
                it.offer(LogMessage(priority, publicTag, logContent))
                Log.v(publicTag, "write $logContent to file")
            }
        }
    }

    private fun initFileLog() {
        if (logQueue != null && coroutineScope != null) {
            return
        }
        
        if (logFileDirPath.isNullOrEmpty()) {
            return
        }

        val logFileDir = File(logFileDirPath!!)
        if (!logFileDir.exists()) {
            if (!logFileDir.mkdir()) {
                Log.e(publicTag, "create log dir failed")
                return
            }
        }

        logQueue = LinkedBlockingQueue<LogMessage>(10000)
        coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        startPeriodicFlush()
        d(publicTag, "logger init success, log path: $logFileDirPath")
        clearOldLog()
    }

    /**
     * 删除过去两天以前所有的日志文件
     */
    private fun clearOldLog() {
        if (logFileDirPath.isNullOrEmpty()) {
            return
        }

        coroutineScope?.launch {
            val logFileDir = File(logFileDirPath!!)
            val files = logFileDir.listFiles() ?: return@launch

            val today: String = fileNameDateFormat.format(Date())
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            val yesterday: String = fileNameDateFormat.format(calendar.time)

            for (file in files) {
                val fileName = file.name
                if (fileName.endsWith(LOG_FILE_SUFFIX)) {
                    if (fileName.length < 8) {
                        continue
                    }

                    val fileDate = fileName.substring(0, 8)

                    if (!fileDate.startsWith(today) && !fileDate.startsWith(yesterday)) {
                        if (!file.delete()) {
                            Log.w(
                                publicTag,
                                "Delete old log file failed: $fileName"
                            )
                        } else {
                            Log.d(
                                publicTag,
                                "Delete old log file success: $fileName"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startPeriodicFlush() {
        coroutineScope?.launch {
            while (true) {
                flushMaxLogToFile()
                delay(WRITE_PERIOD)
            }
        }
    }

    private fun flushMaxLogToFile() {
        flushLogToFile(MAX_BATCH_SIZE)
    }

    private fun flushAllLogToFile() {
        flushLogToFile(Int.MAX_VALUE)
    }
    
    private fun flushLogToFile(logCount: Int) {
        if (logQueue == null || logQueue!!.isEmpty()) {
            return
        }

        contentString.setLength(0)
        var count = 0
        var logMessage: LogMessage
        while (count < logCount) {
            logMessage = logQueue?.poll() ?: break
            contentString.append(logMessage.timestamp).append("  ")
                .append(logMessage.head).append("/").append(logMessage.priority).append("  ")
                .append(logMessage.msg).append("\n")
            ++count
        }

        if (count > 0) {
            writeToFile(contentString.toString())
        }
    }

    /**
     * 使用内存映射的方式将 logContent 追加到日志文件末尾
     */
    private fun writeToFile(logContent: String) {
        if (logFileDirPath.isNullOrEmpty()) {
            return
        }

        val logFile = File(
            logFileDirPath,
            LOG_FILE_PREFIX + fileNameDateFormat.format(Date()) + LOG_FILE_SUFFIX
        )

        try {
            if (logFile.exists() && logFile.length() > MAX_FILE_SIZE) {
                val backupFile = File(
                    logFileDirPath,
                    logFile.name.replace(
                        LOG_FILE_SUFFIX, "_${System.currentTimeMillis()}$LOG_FILE_SUFFIX"
                    )
                )
                if (!logFile.renameTo(backupFile)) {
                    e(
                        tag,
                        "rename log file: ${logFile.absolutePath} -> ${backupFile.absolutePath} failed"
                    )
                }
            }

            if (!logFile.exists()) {
                val logDir = logFile.getParentFile() ?: return;

                if (!logDir.exists()) {
                    if (!logDir.mkdirs()) {
                        w(tag, logDir.getName() + " mkdirs failed");
                        return;
                    }
                }

                if (!logFile.createNewFile()) {
                    w(tag, logFile.getName() + " create failed");
                    return;
                }
            }

            RandomAccessFile(logFile, "rw").use { randomAccessFile ->
                randomAccessFile.channel.use { channel ->
                    val fileLength = randomAccessFile.length()
                    val logBytes = logContent.toByteArray()

                    //map 方法：创建一个内存映射缓冲区，映射区域的起始位置为 fileLength（文件末尾，表示在文件末尾增加数据），映射区域的大小为 logBytes.size（要写入的日志内容的字节数）
                    //put 方法：将 logBytes 数据写入到内存映射缓冲区，写入内存映射缓冲区即为写入文件
                    channel.map(
                        FileChannel.MapMode.READ_WRITE,
                        fileLength,
                        logBytes.size.toLong()
                    ).put(logBytes)
                }
            }
        } catch (e: Exception) {
            e("catch ${e::class.java.simpleName} >> ${e.localizedMessage}")
        }
    }

    private fun release() {
        if (coroutineScope == null) {
            return
        }

        try {
            if (logQueue?.isNotEmpty() == true) {
                runBlocking(Dispatchers.IO) {
                    withTimeout(5000) {
                        flushAllLogToFile()
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            e(tag, "写入剩余日志超时")
        } catch (e: Exception) {
            e(tag, "写入剩余日志失败: ${e.message}")
        } finally {
            logQueue?.clear()
            coroutineScope?.cancel()
            Log.d(publicTag, "[$tag] released")
        }
    }
}