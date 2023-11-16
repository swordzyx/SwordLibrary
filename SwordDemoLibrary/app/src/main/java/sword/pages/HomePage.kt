package sword.pages

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.ViewPager

class HomePage(val context: Context): Page {
  override val rootView by lazy { 
    createView()
  }
  
  private fun createView(): View {
    val viewPager = ViewPager(context).apply {
      setBackgroundColor(Color.WHITE)
      adapter =
    }

    return LinearLayout(context).apply {
      contentDescription = "首页"
      val textView = AppCompatTextView(context).apply { 
        text = "登录成功"
        gravity = Gravity.CENTER
      }
      addView(textView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
    }
  }

  
}