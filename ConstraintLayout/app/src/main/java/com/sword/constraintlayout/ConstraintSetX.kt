package com.sword.constraintlayout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class ConstraintSetX: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_set)
    }

    fun onClick(view: View){
        val constraintLayout = view as ConstraintLayout
        //创建一个约束集，从现有的 ConstraintLayout 中先拷贝约束，再通过 connect 函数添加新的约束
        val constraintSet = ConstraintSet().apply {
            //从 xml 中拷贝约束到 constraintSet 中
            clone(constraintLayout)
            //创建一个约束，并添加到 constraintSet 中
            //将 id 为 twitter 的 view 的 bottom 约束到 parent 的 bottom
            connect(R.id.twitter,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
        }
        //将新的约束集 constraintSet 应用到 constarintLayout
        constraintSet.applyTo(constraintLayout)
    }
}