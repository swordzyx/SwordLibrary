package com.example.swordlibrary.io.kotlin

import com.sword.LogUtil
import okio.*
import okio.Path.Companion.toPath
import java.io.File

class OkioIO {
  private val tag = "OkioIO"
  
  
  fun readFile(path: String, lineCollector: LineCollector?): String {
    val startTime = System.currentTimeMillis()
    val result = StringBuilder()
    
    File(path).source().buffer().use { bufferedSource ->
      while (true) {
        val line = bufferedSource.readUtf8Line() ?: break
        result.append("$line\n")
        LogUtil.info(tag, line)
      }
    }
    LogUtil.debug(tag, "读取 $path 耗时 ${System.currentTimeMillis() - startTime}ms")
    return result.toString()
  }
  
  fun readLine(path: Path, lineCollector: LineCollector?) {
    val startTime = System.currentTimeMillis()
    FileSystem.SYSTEM.source(path).buffer().use { bufferedSource -> 
      while (true) {
        val line = bufferedSource.readUtf8Line() ?: break
        lineCollector?.readLine(line)
      }
    }
    LogUtil.debug(tag, "读取 $path 耗时 ${System.currentTimeMillis() - startTime}ms")
  }
  
  interface LineCollector {
    fun readLine(line: String)
  }
}