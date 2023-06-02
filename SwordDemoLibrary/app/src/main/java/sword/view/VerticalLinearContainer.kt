package sword.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.sword.LogUtil
import com.sword.dp

abstract class VerticalLinearContainer(context: Context, attrs: AttributeSet? = null) :
  LinearLayout(context, attrs), INavigationLayout {
  
  protected val tag: String = javaClass.canonicalName!!
  protected val container = FrameLayout(context).apply {
    visibility = View.GONE
  }.apply { 
    this@VerticalLinearContainer.addView(this, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
  }
  //需要放在初始化代码块之前，否则 init 代码块中使用这个变量时，变量还未初始化，得到的值是 0
  protected val defaultMargin = dp(2)
  
  init {
    orientation = VERTICAL
  }
  
  override fun onBackPressed(): Boolean {
    LogUtil.debug(tag, "onBackPressed")
    if (container.visibility != View.GONE) {
      container.visibility = View.GONE
    }
    return true
  }

  protected fun LayoutParams.setVerticalMargin(verticalMargin: Int = defaultMargin): LayoutParams {
    setMargins(0, verticalMargin, 0, verticalMargin)
    return this
  }

}