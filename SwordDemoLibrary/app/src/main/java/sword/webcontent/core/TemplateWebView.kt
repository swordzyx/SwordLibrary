package sword.webcontent.core

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class TemplateWebView(context: Context, attr: AttributeSet? = null) : BaseWebView(context, attr) {
  override fun release() {
    (parent as ViewGroup?)?.removeView(this)
    removeAllViews()
    evaluateJavascript("javascript:clearData()"){}
  }

  override fun destroy() {
    super.destroy()
  }
}