package sword.pages

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.example.swordlibrary.R
import sword.MainActivity
import sword.dp
import sword.logger.SwordLog
import sword.theme.Theme
import sword.view.ViewModuleMainPage

class HomeView(private val activity: Activity){
  private val tag = "HomeView"
  init {
    if (activity is MainActivity) {
      activity.addBackListener(MainActivity.BackPressedListener {
        SwordLog.debug(tag, "onBackPress")
        val decorView = activity.window.decorView

        if (currentView != null && decorView is ViewGroup) {
          decorView.removeView(currentView)
          currentView = null
          if (mainContainer.visibility != View.VISIBLE) {
            mainContainer.visibility = View.VISIBLE
          }
          return@BackPressedListener true
        }

        false
      })
    }
  }
  
  val mainContainer by lazy {
    mainView()
  }
  private var currentView: View? = null 
    set(value) {
      field = value
      val decorView = activity.window.decorView
      if (decorView is ViewGroup) {
        if (field != null) {
          val layoutParams = field!!.layoutParams ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          (activity.window.decorView as FrameLayout).addView(field, layoutParams)
        }
      }
    }
  
  @SuppressLint("SetTextI18n", "InflateParams")
  private fun mainView(): ViewGroup {
    val resultView = LinearLayout(activity).apply { 
      orientation = LinearLayout.VERTICAL
    }
    
    val originLayoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
      topMargin = 5.dp
    }
    
    val photoPickerModuleButton = Theme.createCircleButton(activity).apply { 
      text = "选取图片"
      setOnClickListener {
        currentView = LayoutInflater.from(context).inflate(R.layout.view_photo_picker, null)
      }
    }
    resultView.addView(photoPickerModuleButton, originLayoutParams)
    
    val customViewModuleButton = Theme.createCircleButton(activity).apply { 
      text = "自定义View"
      setOnClickListener { 
        currentView = ViewModuleMainPage(activity).mainContainer
      }
    }
    resultView.addView(customViewModuleButton, originLayoutParams)
    
    return resultView
  }

}