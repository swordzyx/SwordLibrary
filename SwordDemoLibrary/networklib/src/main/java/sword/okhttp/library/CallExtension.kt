package sword.okhttp.library

import okhttp3.Call

fun Call.string(): String {
  val request = request()
  return request.toString()
}