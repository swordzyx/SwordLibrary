package sword

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import leakcanary.LeakCanary

class SwordApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    globalContext = applicationContext
  }

  companion object {
    @SuppressLint("StaticFieldLeak")
    @JvmStatic
    var globalContext: Context? = null
      private set
  }
}