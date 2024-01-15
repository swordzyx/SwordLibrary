package sword

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import sword.devicedetail.getCpuModel
import sword.logger.SwordLog
import sword.pages.ContentPage
import sword.utils.AndroidFileSystem
import sword.view.*
import java.util.Stack

class MainActivity : AppCompatActivity() {
  private val tag = "MainActivity"
  private var contentPage: ContentPage? = null
  private val rootView: View?
    get() {
      return contentPage?.rootView
    }
  private val backPressedListenerList = mutableListOf<BackPressedListener>()

  @SuppressLint("SetTextI18n", "InflateParams")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initWindowSize(this)
    
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
      fullScreenByFlag(window)
    }

    contentPage = ContentPage(this)
    setContentView(rootView)
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      fullScreenByInsetController(window)
    }
    
    SwordLog.debug(tag, getCpuModel())
    AndroidFileSystem.printFileSystemInfo(this)
    
  }
  
  fun addBackListener(listener: BackPressedListener) {
    backPressedListenerList.add(listener)
  } 
  
  
  @Deprecated("Deprecated in Java")
  override fun onBackPressed() {
    SwordLog.debug(tag, "onBackPress")
    
    for (i in backPressedListenerList.size - 1 downTo 0) {
      if (backPressedListenerList[i].onBackPressed()) {
        return
      }
    }
    super.onBackPressed()
  }
  
  fun interface BackPressedListener {
    fun onBackPressed(): Boolean
  }
}