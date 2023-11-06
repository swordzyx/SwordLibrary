package sword.net.okhttp.library

import okhttp3.HttpUrl

class SwordRequest(
  @get: JvmName("url") val url: HttpUrl,
  @get: JvmName("method") val method: String,
  @get: JvmName("headers") val headers: SwordHeaders,
  @get: JvmName("body") val body: SwordRequestBody
) {
  
}