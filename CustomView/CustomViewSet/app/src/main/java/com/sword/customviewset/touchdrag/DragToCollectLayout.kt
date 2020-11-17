package com.sword.customviewset.touchdrag

import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.drat_to_collect.view.*

class DragToCollectLayout(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    val longClick = OnLongClickListener { v ->
        val imageData = ClipData.newPlainText("name", v.contentDescription)
        ViewCompat.startDragAndDrop(v, imageData, DragShadowBuilder(v), null, 0)
    }

    val collectDragListener = CollectDragListener()

    override fun onFinishInflate() {
        super.onFinishInflate()
        avatarView.setOnLongClickListener(longClick)
        logoView.setOnLongClickListener(longClick)
        collectorLayout.setOnDragListener(collectDragListener)
    }

    inner class CollectDragListener: OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when(event.action) {
                DragEvent.ACTION_DROP -> if(v is LinearLayout) {
                    val textView = TextView(context)
                    textView.textSize = 16f
                    textView.text = event.clipData.getItemAt(0).text
                    v.addView(textView)
                }
            }
            return true
        }
    }

}

