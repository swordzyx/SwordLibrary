package sword.okhttp.library

import java.util.logging.Level
import java.util.logging.Logger

class Platform {
  private val logger = Logger.getLogger(SwordOkHttpClient::class.java.name)
  
  fun getStackTraceForCloseable(closer: String?): Any? {
    return when {
      logger.isLoggable(Level.FINE) -> Throwable(closer)
      else -> null
    }
  }
  
  companion object {
    private val platform = Platform()
    fun get(): Platform {
      return platform
    }
  }
}