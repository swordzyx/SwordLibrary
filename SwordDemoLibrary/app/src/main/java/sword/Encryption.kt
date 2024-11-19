package sword

import android.text.TextUtils
import android.util.Base64
import android.util.Log
import java.lang.StringBuilder
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
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

/**
 * 加密方法
 */
@Throws(Exception::class)
fun aesEncrypt(data: String, key: String, iv: String): String {
  try {
    // 算法/模式/补码方式 NoPadding PkcsPadding
    val cipher = Cipher.getInstance("AES/CBC/NoPadding")
    val blockSize = cipher.blockSize

    val dataBytes = data.toByteArray()
    var plaintextLength = dataBytes.size
    if (plaintextLength % blockSize != 0) {
      plaintextLength += (blockSize - (plaintextLength % blockSize))
    }

    val plaintext = ByteArray(plaintextLength)
    System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.size)

    val keySpec = SecretKeySpec(key.toByteArray(), "AES")
    val ivSpec = IvParameterSpec(iv.toByteArray())

    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
    val encrypted = cipher.doFinal(plaintext)

    return String(base64Encode(encrypted))
  } catch (e: Exception) {
    Log.e("Encryption", "encrypt error, error: " + e.message)
    return ""
  }
}

/**
 * 解密方法
 *
 * @param data 要解密的数据
 * @param key  解密key
 * @param iv   解密iv
 * @return 解密的结果
 */
fun desEncrypt(data: String, key: String, iv: String): String? {
  try {
    val encrypted: ByteArray = base64Encode(data)

    val cipher = Cipher.getInstance("AES/CBC/NoPadding")
    val keySpec = SecretKeySpec(key.toByteArray(), "AES")
    val ivSpec = IvParameterSpec(iv.toByteArray())

    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

    val original = cipher.doFinal(encrypted)

    return String(original)
  } catch (e: java.lang.Exception) {
    Log.e("Encryption", "decrypt error, error: " + e.message)
  }
  return null
}

fun base64Encode(input: String): ByteArray {
  return base64Encode(input.toByteArray())
}

fun base64Encode(input: ByteArray): ByteArray {
  if (input.isEmpty()) return ByteArray(0)
  return Base64.encode(input, Base64.NO_WRAP)
}


