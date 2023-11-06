package sword.net.okhttp.library

import okhttp3.ResponseBody

class SwordResponse(
  @get: JvmName("request") val request: SwordRequest,
  @get: JvmName("body") val body: ResponseBody? 
) {
}