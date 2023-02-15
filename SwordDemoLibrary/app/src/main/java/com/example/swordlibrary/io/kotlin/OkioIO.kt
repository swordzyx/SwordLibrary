package com.example.swordlibrary.io.kotlin

import android.os.FileUtils
import com.sword.LogUtil
import okio.*
import okio.Path.Companion.toPath
import java.io.File


fun main() {
    //println(OkioIO.readFile("./gradle.properties"))
    println("拷贝 ${OkioIO.copyFile("./gradle.properties", "./gradle_copy1.properties")} 字节")
}

object OkioIO {
    private const val tag = "OkioIO"

    fun copyFile(sourcePath: String, targetPath: String): Long {
        val sourceFile = File(sourcePath)
        if (!sourceFile.exists()) {
            println("源文件不存在")
            return -1
        }

        val targetFile = File(targetPath).apply {
            if (exists()) {
                delete()
            } else if (parentFile != null && !parentFile!!.exists()) {
                parentFile!!.mkdirs()
            }
            createNewFile()
        }

        sourceFile.source().buffer().use { input ->
            targetFile.sink().buffer().use { output ->
                val count = output.writeAll(input)
                output.flush()
                return count
            }
        }
    }

    fun readFile(path: String): String {
        val startTime = System.currentTimeMillis()
        val result = StringBuilder()

        File(path).also { file ->
            if (!file.exists())
                return ""

            file.source().buffer().use { bufferedSource ->
                while (true) {
                    val line = bufferedSource.readUtf8Line() ?: break
                    result.append("$line\n")
                    //LogUtil.info(tag, line)
                }
            }
            println("读取 $path 耗时 ${System.currentTimeMillis() - startTime}ms")
            return result.toString()
        }
    }

    fun readLine(path: String, lineCollector: LineCollector?) {
        readLine(path.toPath(), lineCollector)
    }

    fun readLine(path: Path, lineCollector: LineCollector?) {
        val startTime = System.currentTimeMillis()
        //FileSystem.SYSTEM.read(path) {} 这种写法等价于 FileSystem.SYSTEM.source(path).buffer()
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