package com.sword.logger

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * 参考：
 * - https://github.com/Blankj/AndroidUtilCode  ---  LogUtils
 * - https://devv.ai/search?threadId=e3hss7be0nb4
 * - 参考涂鸦插件 LogUtils.java
 * - B3: LogUtils.java
 */
object LogUtils {
    var publicTag = "SwordLibrary"
    private val isDebug = true
    private val logToFile = false

    private const val LOG_FILE_PREFIX = "log_"
    private const val LOG_FILE_SUFFIX = ".log"
    private const val MAX_FILE_SIZE = 10 * 1024 * 1024 // 10MB
    private const val BUFFER_SIZE = 8192 // 8KB
    private const val MAX_BATCH_SIZE = 100 //最大批量写入日志条数

    private var logFileDirPath: String? = null

    /**
     * 使用 ThreadLocal，避免多线程下 SimpleDateFormat 的线程安全问题
     */
    @SuppressLint("ConstantLocale")
    private val fileNameDateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    @SuppressLint("ConstantLocale")
    private val logDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    /**
     * 使用低优先级线程处理日志的写入
     */
    private val threadCount = AtomicInteger(1)
    private val logExecutor = Executors.newScheduledThreadPool(1, object : ThreadFactory {
        override fun newThread(r: Runnable?): Thread {
            return Thread(r, "LogWriter-${threadCount.getAndIncrement()}").apply {
                priority = Thread.MIN_PRIORITY
            }
        }
    })

    /**
     * 日志缓冲区
     * LinkedBlockingQueue 是一个线程安全的阻塞队列，当队列为空时，从队列中获取元素的操作会被阻塞
     */
    private val logQueue = LinkedBlockingQueue<LogMessage>(10000)
    private val batchBuilder = StringBuilder(BUFFER_SIZE) 
    
    private data class LogMessage(
        val priority: Int,
        val head: String,
        val msg: String,
        val timestamp: Long = System.currentTimeMillis(),
    )

    fun init(context: Context) {
        if (logToFile) {
            logFileDirPath = "${context.filesDir.absolutePath}/logs"
            logFileDirPath.let {  
                
            }
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
            Log.INFO -> if (isDebug) { Log.i(publicTag, logContent) }
            Log.WARN -> { Log.w(publicTag, logContent) }
            Log.ERROR -> { Log.e(publicTag, logContent) }
        }

        if (logToFile || priority == Log.WARN || priority == Log.ERROR) {
            Log.d(publicTag, "write $priority - $head - $msg to file")
            logQueue.offer(LogMessage(priority, head, msg))
        }
    }

 
    /**
     * 批量写入 LogMessage 到日志文件，最多写入 [MAX_BATCH_SIZE] 条
     */
    private fun flushLogToFile() {
        //如果队列为空，返回
        if (logQueue.isEmpty()) {
            return
        }

        batchBuilder.setLength(0)
        var count = 0
        var logMessage: LogMessage
        //拼接 LogMessage 到 StringBuilder 中
        while (count < MAX_BATCH_SIZE) {
            logMessage = logQueue.poll() ?: break
            //时间 tag/等级 内容
            batchBuilder.append(logMessage.timestamp).append("  ")
                .append(logMessage.head).append("/").append(logMessage.priority)
                .append(logMessage.msg).append("\n")
            ++count
        }

        //3. 将 StringBuilder 中的内容写入日志文件
        writeToFile(batchBuilder.toString())
    }

    /**
     * 使用内存映射的方式将 logContent 追加到日志文件末尾
     */
    private fun writeToFile(logContent: String) {
        if (logFileDirPath.isNullOrEmpty()) {
            return
        }

        //日志文件名称: log_yyyyMMdd.log
        val logFile = File(
            logFileDirPath,
            LOG_FILE_PREFIX + fileNameDateFormat.format(Date()) + LOG_FILE_SUFFIX
        )
        
        try {
            if (logFile.exists() && logFile.length() > MAX_FILE_SIZE) {
                val backupFile = File(
                    logFileDirPath,
                    LOG_FILE_PREFIX + fileNameDateFormat.format(Date()) + "_" + System.currentTimeMillis() + LOG_FILE_SUFFIX
                )
                if (logFile.renameTo(backupFile)) {
                    Log.d(publicTag, "rename log file: ${logFile.absolutePath} -> ${backupFile.absolutePath}")
                    logFile.createNewFile()
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
            e.printStackTrace()
        }
    }

    private fun clearLog() {
        if (logFileDirPath == null) {
            return
        }

        val logFileDir = File(logFileDirPath!!)
        if (!logFileDir.exists()) {
            return
        }

        logFileDir.listFiles()?.forEach {
            if (it.isFile && it.name.startsWith(LOG_FILE_PREFIX) && it.name.endsWith(LOG_FILE_SUFFIX)) {
                Log.d(publicTag, "delete log file: ${it.absolutePath}")
                it.delete()
            }
        }
    }
}