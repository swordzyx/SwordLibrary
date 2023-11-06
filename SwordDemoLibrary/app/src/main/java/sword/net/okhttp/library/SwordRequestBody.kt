package sword.net.okhttp.library

import java.io.Closeable

abstract class SwordRequestBody: Closeable {
  open fun isOneshot(): Boolean = false
  
  override fun close() {
    TODO("Not yet implemented")
  }
}