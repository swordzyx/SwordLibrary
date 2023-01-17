package com.example.swordlibrary.logger

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.sword.LogUtil
import com.sword.dp

open class LoggerActivity: AppCompatActivity() {
  private val tag = this::class.java.name
  open var debug = LogUtil.isDebug()

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    
    if (debug) {
      LogUtil.debug(tag, "onConfigurationChanged, newConfig: $newConfig")
      LogUtil.debug(
        tag,
        "newConfig width: " + dp(newConfig.screenWidthDp) + ", height: " + dp(newConfig.screenHeightDp)
      )
    }
  }
}