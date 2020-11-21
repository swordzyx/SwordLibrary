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

        //遍历 referencedIds 中保存的 view 的 id，并为这些 id 所对应的 view 添加动画
        referencedIds.forEach {
            val view = container.getViewById(it)
            //计算传入的两个参数的平方和，然后开根号
            val radius = hypot(view.width.toDouble(), view.height.toDouble()).toInt()

            //以圆的形式逐渐显示图片，圆心坐标为(0, 0)，这是相对于 view 的坐标
            ViewAnimationUtils.createCircularReveal(view, 0, 0, 0f, radius.toFloat())
                .setDuration(20000L)
                .start()
        }
    }
}