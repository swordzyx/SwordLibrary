package com.sword.constraintlayout

import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class ConstraintX1: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_start)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun onClick(view: View) {
        val constraintLayout = view as ConstraintLayout

        val constraintSet = ConstraintSet().apply {
            //如果新的布局中有些控件没有 id，通过将 isForceId 置为 false 来防止报错
            isForceId = false
            //从 activity_constraint_end 布局文件中克隆出约束集，保存到 constraintSet 中
            clone(this@ConstraintX1, R.layout.activity_constraint_end)
        }
        TransitionManager.beginDelayedTransition(constraintLayout)
        //将 constraintSet 中保存的约束应用到 view 中。
        constraintSet.applyTo(constraintLayout)
    }


}