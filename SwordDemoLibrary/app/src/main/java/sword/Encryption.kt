package sword

import android.text.TextUtils
import java.lang.StringBuilder
import java.security.MessageDigest
import java.util.*
import kotlin.experimental.and

private val DIGITS = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

fun signParamWithSalt(content: TreeMap<String, String>, salt: String?): String {
  val paramSB = StringBuilder().apply {
    content.forEach {
      append("&${it.key}=${it.value.trim()}")
    }
  }

  val paramString = paramSB.toString().replaceFirst("&", "")
  
  if (!TextUtils.isEmpty(salt)) {
    paramSB.append("&salt=${salt}")
  }
  
  return "${paramString}&sign=${md5(paramSB.toString().replaceFirst("&", ""))}"
}

private fun md5(content: String): String {
  val md = MessageDigest.getInstance("MD5")
  md.update(content.toByteArray())
  println(content)
  return md.digest().toHexString1()
}

fun ByteArray.toHexString(): String {
  forEach { 
    println(it)
  }
  println()
  val out = CharArray((size shl 1))

  for (i in 0 until size) {
    (this[i] and (0xff).toByte()).toInt().let {
      println("${this[i]} -- $i -- $it")
      out[i * 2] = DIGITS[it ushr 4]
      out[i * 2 + 1] = DIGITS[it and 0x0F]
    }
  }
  
  return String(out)
}

fun ByteArray.toHexString1(): String {
  val hexString = StringBuilder()
  forEach { 
    var c = Integer.toHexString(it.toInt())
    //println("byte -> hexString: $it -> $c")
    if (it < 0x10) {
      c = "0$c"
    }
    hexString.append(c)
  }
  return hexString.toString()
}

