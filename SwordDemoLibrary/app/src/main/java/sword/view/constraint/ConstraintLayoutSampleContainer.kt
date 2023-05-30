package sword.view.constraint

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.example.swordlibrary.R
import com.sword.LogUtil
import sword.utils.createButtonToShowViewInContainer

class ConstraintLayoutSampleContainer(context: Context, attributeSet: AttributeSet? = null): LinearLayout(context, attributeSet) {
  private val tag = "ConstraintLayoutSampleContainer"
  private val container = FrameLayout(context).apply { 
    visibility = View.GONE
  }
  
  init {
    addView(container, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    addBaseAttrSample()
    addCircularRevealConstraintHelperSample()
  }
  
  @SuppressLint("InflateParams")
  private fun addBaseAttrSample() {
    val view = LayoutInflater.from(context).inflate(R.layout.constraint_base_attr_sample, null)
    addView(
      createButtonToShowViewInContainer(
        "约束布局基础属性示例",
        view,
        container),
      LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    )
  }

  private fun addCircularRevealConstraintHelperSample() {
    val view = LayoutInflater.from(context).inflate(R.layout.circular_reveal_constraint_helper, null)
    addView(
      createButtonToShowViewInContainer(
        "ConstraintHelper 示例",
        view,
        container),
      LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    )
  }

  override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
    val actionString = when(event.action) {
      KeyEvent.ACTION_DOWN -> "按下"
      KeyEvent.ACTION_UP -> "抬起"
      else -> "其他操作"
    }
    val keyName = when(keyCode) {
      KeyEvent.KEYCODE_BACK -> "返回键"
      else -> keyCode.toString()
    }
    LogUtil.debug(tag, "onKeyDown, $actionString $keyName")
    if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
      if (container.visibility != View.GONE) {
        container.visibility = View.GONE
      }
      return true
    }
    return super.onKeyDown(keyCode, event)
  }
}