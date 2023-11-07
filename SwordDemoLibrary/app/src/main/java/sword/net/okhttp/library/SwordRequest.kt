package sword.net.okhttp.library

import okhttp3.HttpUrl
import java.io.Closeable

class SwordRequest(
  @get: JvmName("url") val url: HttpUrl,
  @get: JvmName("method") val method: String,
  @get: JvmName("headers") val headers: SwordHeaders,
  @get: JvmName("body") val body: SwordRequestBody
) {
  
  
}


abstract class SwordRequestBody: Closeable {
  open fun isOneshot(): Boolean = false

  override fun close() {
    TODO("Not yet implemented")
  }
}