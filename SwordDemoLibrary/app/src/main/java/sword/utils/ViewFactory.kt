package sword.utils

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import com.example.swordlibrary.R
import com.sword.dp

/**
 * 创建一个蓝底白字的椭圆型 Button
 * [buttonName]: 按钮显示的文本
 * [showView]: 点击按钮后要添加到 [container] 中并显示的 view
 */
fun createButtonToShowViewInContainer(
  buttonName: String,
  showView: View,
  container: ViewGroup
): AppCompatButton {
  val padding = dp(20);
  return AppCompatButton(container.context).apply {
    text = buttonName
    isAllCaps = false

    setPadding(padding, 0, padding, 0)
    setBackgroundResource(R.drawable.button_background_circle_corner)
    setTextColor(Color.WHITE)
    setOnClickListener {
      container.visibility = View.VISIBLE
      container.addView(
        showView,
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.MATCH_PARENT
        )
      )

      container.hideChildren()
      if (showView.visibility != View.VISIBLE) {
        showView.visibility = View.VISIBLE
      }
    }
  }
}

private fun ViewGroup.hideChildren() {
  children.forEach { child ->
    if (child.visibility != View.GONE) {
      child.visibility = View.GONE
    }
  }
}