package com.example.utilclass.net.dongliu.apk.parser

import android.text.TextUtils
import java.lang.StringBuilder
import java.security.MessageDigest
import java.util.*
import kotlin.experimental.and

private val DIGITS = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

fun signParamWithSalt(content: TreeMap<String, String>, salt: String?): String {
  val paramString = StringBuilder().apply {
    content.forEach {
      append("&${it.key}=${it.value.trim()}")
    }
    
    if (!TextUtils.isEmpty(salt)) {
      append("&salt=${salt}")
    }
  }.toString()
  
  return md5(paramString.replaceFirst("&", ""))
}

private fun md5(content: String): String {
  val md = MessageDigest.getInstance("MD5")
  md.update(content.toByteArray())
  return md.digest().toHexString()
}

fun ByteArray.toHexString(): String {
  val out = CharArray((size shl 1))

  for (i in 0 until size) {
    (this[i] and (0xff).toByte()).toInt().let {
      out[i * 2] = DIGITS[it ushr 4]
      out[i * 2 + 1] = DIGITS[it and 0x0F]
    }
  }
  
  return String(out)
}