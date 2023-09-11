package sword.logger

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import sword.SwordLog
import com.sword.dp

open class LoggerActivity: AppCompatActivity() {
  private val tag = this::class.java.name
  open var debug = SwordLog.isDebug()

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    
    if (debug) {
      SwordLog.debug(tag, "onConfigurationChanged, newConfig: $newConfig")
      SwordLog.debug(
        tag,
        "newConfig width: " + dp(newConfig.screenWidthDp) + ", height: " + dp(newConfig.screenHeightDp)
      )
    }
  }
}