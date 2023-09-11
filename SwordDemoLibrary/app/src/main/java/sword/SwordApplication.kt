package sword

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.github.moduth.blockcanary.BlockCanary
import sword.blockcanary.AppBlockCanaryContext

class SwordApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    globalContext = applicationContext
    
    //BlockCanary 初始化
    BlockCanary.install(this, AppBlockCanaryContext()).start();
  }

  companion object {
    @SuppressLint("StaticFieldLeak")
    @JvmStatic
    var globalContext: Context? = null
      private set
  }
}