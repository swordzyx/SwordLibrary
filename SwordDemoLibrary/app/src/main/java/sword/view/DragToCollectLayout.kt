package sword.view

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.DragEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import sword.logger.SwordLog

class DragToCollectLayout(context: Context, attrs: AttributeSet? = null): ConstraintLayout(context, attrs) {
    private val tag = "DragToCollectLayout"

    override fun onFinishInflate() {
        super.onFinishInflate()
        children.forEach { child ->
            if (child is ImageView) {
                child.setOnLongClickListener { view ->
                    val clipData = ClipData.newPlainText("desc", view.contentDescription)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        view.startDragAndDrop(clipData, DragShadowBuilder(view), view, 0)
                    } else {
                        view.startDrag(clipData, DragShadowBuilder(view), view, 0)
                    }
                    true
                }
            } else if (child is ViewGroup) {
                child.setOnDragListener(dragListener)
            }
        }
    }

    private val dragListener = OnDragListener { v, event ->
        when(event.action) {
            DragEvent.ACTION_DROP -> {
                val clipdata = event.clipData
                SwordLog.debug(tag, "拖拽内容：$clipdata")

                for (i in 0 until clipdata.itemCount) {
                    val description = clipdata.description.getMimeType(i)
                    val item = clipdata.getItemAt(i)
                    SwordLog.debug(tag, "clipData $i, mime type: $description, data: ${item.text}")

                    val textView = TextView(context)
                    textView.text = item.text
                    (v as ViewGroup).addView(textView)
                }
            }
        }
        true
    }
}