package sword.data

import android.text.TextUtils
import java.util.*

/**
 * 生成uuid
 */
fun buildUUID(name: String?): String {
  var uuid: String = if (TextUtils.isEmpty(name)) {
    UUID.randomUUID().toString().replace("-".toRegex(), "")
  } else {
    UUID.fromString(name).toString()
  }
  uuid += System.currentTimeMillis()
  return uuid
}

var s = "qwertyuiopasdfghjklzxcvbnmQWERRTYUIOPASDFGHJKLZXCVBNM1234567890"
fun randomString(count: Int): String {
  val random = Random()
  val result = StringBuilder()
  for (i in 0 until count) {
    result.append(s[random.nextInt(s.length)])
  }
  return result.toString()
}