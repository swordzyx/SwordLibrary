package com.example.constraintlayout

import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class ConstraintSetStart: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_start)
    }

    //设置 R.id.twitter 的 Bottom 约束为 parent 的 Bottom
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun onClick(view: View) {
        val constraintLayout = view as ConstraintLayout

        val constraintSet = ConstraintSet().apply {
            //如果 activity_constraint_end 中有没有设置 Id 的 view ，如果 isForceId 为 true ，则会报错，因此要将 isForceId 设为 fasle
            isForceId = false
            clone(this@ConstraintSetStart, R.layout.activitry_constraint_end)
        }

        //在布局改变之前，调用 TransitionManager.beginDelayedTransition(constraintLayout) ，里面传入 ConstraintLayout 容器，即可方便的实现过度动画
        TransitionManager.beginDelayedTransition(constraintLayout)
        constraintSet.applyTo(constraintLayout)
    }
}