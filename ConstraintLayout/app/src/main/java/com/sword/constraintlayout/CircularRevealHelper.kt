package com.sword.constraintlayout

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.ViewAnimationUtils
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.hypot


class CircularRevealHelper(context:Context, attrs: AttributeSet): ConstraintHelper(context, attrs) {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun updatePostLayout(container: ConstraintLayout) {
        super.updatePostLayout(container)

        referencedIds.forEach {
            val view = container.getViewById(it)
            val radius = hypot(view.width.toDouble(), view.height.toDouble()).toInt()

            ViewAnimationUtils.createCircularReveal(view, 0, 0, 0f, radius.toFloat())
                .setDuration(20000L)
                .start()
        }
    }
}