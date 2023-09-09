package sword

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary

class SwordApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    globalContext = applicationContext
    
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }
    LeakCanary.install(this)
  }

  companion object {
    @SuppressLint("StaticFieldLeak")
    var globalContext: Context? = null
      private set
  }
}