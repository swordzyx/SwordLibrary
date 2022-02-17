package com.example.utilclass.apk.parse

import java.io.File
import java.util.zip.ZipFile

class ApkFile constructor(apkPath: String) {
  //默认使用 utf-8 的编码格式
  private val apkFile: File = File(apkPath)
  private val apkZip: ZipFile = ZipFile(apkFile)
  

  fun getStringFileData(filePath: String): String? {
    return getByteArrayFileData(filePath)?.let { String(it) }
  }

  private fun getByteArrayFileData(filePath: String): ByteArray? {
    val entry = apkZip.getEntry(filePath) ?: return null

    val input = apkZip.getInputStream(entry) ?: return null

    return input.readBytes()
  }
}