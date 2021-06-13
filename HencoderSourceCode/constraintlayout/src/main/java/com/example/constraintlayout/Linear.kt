package com.example.constraintlayout

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.VirtualLayout

//VirtualLayout 继承自 ConstraintHelper ，Linear 是一个自定义的 ConstraintHelper
class Linear(context: Context, attrs: AttributeSet): VirtualLayout(context, attrs) {
    //懒加载
    private val constraintSet: ConstraintSet by lazy {
        ConstraintSet().apply {
            isForceId = false
        }
    }

    //此方法在加载布局之前调用
    override fun updatePreLayout(container: ConstraintLayout?) {
        super.updatePreLayout(container)
        constraintSet.clone(container)

        val viewIds = referencedIds
        for (i in 1 until mCount) {
            val current = viewIds[i]
            val before = viewIds[i - 1]

            constraintSet.connect(current, ConstraintSet.START, before, ConstraintSet.START)
            constraintSet.connect(current, ConstraintSet.TOP, before, ConstraintSet.BOTTOM)

            constraintSet.applyTo(container)
        }
    }
}