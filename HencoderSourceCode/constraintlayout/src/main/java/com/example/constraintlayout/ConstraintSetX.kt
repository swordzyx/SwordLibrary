package com.example.constraintlayout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class ConstraintSetX: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_set)
    }

    //设置 R.id.twitter 的 Bottom 约束为 parent 的 Bottom
    fun onClick(view: View) {
        val constraintLayout = view as ConstraintLayout
        //创建一个 ConstraintSet 对象
        val constraintSet = ConstraintSet().apply {
            //获取一份 ConstraintLayout 中的约束
            clone(constraintLayout)
            //根据需要更改约束
            connect(
                R.id.twitter,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
        }
        //将更改之后的约束应用到布局中
        constraintSet.applyTo(constraintLayout)
    }
}