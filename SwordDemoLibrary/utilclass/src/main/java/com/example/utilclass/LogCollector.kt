package com.example.utilclass

import android.os.Environment
import android.widget.Toast
import java.io.*

class LogCollector {
    private var logFile: File
    
    init {
        val logFileParent = "${Environment.getExternalStorageDirectory().absoluteFile}${File.separator}logfiles"
        val parentDir = File(logFileParent)
        
        val logFilePath = "$logFileParent${File.separator}XlcwLog.txt"
        logFile = File(logFilePath)
        
        if (!parentDir.exists()) {
            parentDir.mkdirs()

            logFile.createNewFile()
            logFile.setWritable(true)
        }
        LogUtil.debug("logFilePath: $logFilePath")
    }
    
    fun collectLog() {
        Thread {
            val args = arrayOf("logcat", "-d")
            val proc = Runtime.getRuntime().exec(args)
            val reader = BufferedReader(InputStreamReader(proc.inputStream))
            val writer = BufferedWriter(OutputStreamWriter(FileOutputStream(logFile)))
            reader.readLines().forEach { line ->
                writer.write("$line\n")
            }
            reader.close()
            writer.close()
            LogUtil.debug("collect complete")
        }.start()
    }
}