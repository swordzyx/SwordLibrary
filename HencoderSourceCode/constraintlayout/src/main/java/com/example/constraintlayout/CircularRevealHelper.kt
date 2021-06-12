package com.example.constraintlayout

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.ViewAnimationUtils
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.hypot

class CircularRevealHelper(context: Context, attrs: AttributeSet) : ConstraintHelper(context, attrs) {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun updatePostLayout(container: ConstraintLayout) {
        super.updatePostLayout(container)

        //ConstraintHelper 中的 mIds 这个成员记录了 view 组中所有的 view 的 id，但是它是一个固定长度为 32 的数组，不能同通过 mIds 来遍历 view ，而是要通过 getReferenceIds() 这个方法，该方法回将 mIds 中所有有效的 view id 拷贝到一个新的数组中，然后将该数组返回
        referencedIds.forEach {
            val view = container.getViewById(it)
            //计算 width^2 + height^2 开根号
            val radius = hypot(view.width.toDouble(), view.height.toDouble()).toInt()

            /**
             * view：执行动画的 view
             * 0：裁剪圆的圆心的 X 坐标
             * 0：裁剪圆的圆心的 Y 做白哦
             * 0f：裁剪圆的起始半径
             * radius.toFloat()：裁剪圆的结束半径
             */
            ViewAnimationUtils.createCircularReveal(view, 0, 0, 0f, radius.toFloat())
                .setDuration(2000L)
                .start()
        }
    }

}