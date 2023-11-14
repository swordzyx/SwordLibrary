package sword.okhttp.library

import java.io.Closeable

class SwordResponse(
  @get: JvmName("request") val request: SwordRequest,
  @get: JvmName("body") val body: SwordRequestBody?,
  @get: JvmName("code") val code: Int
): Closeable {
  override fun close() {
    checkNotNull(body) {"response is not eligible for a body and must not be closed"}.close()
  }
}