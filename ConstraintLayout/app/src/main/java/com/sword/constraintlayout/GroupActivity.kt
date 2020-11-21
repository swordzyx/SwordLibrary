package com.sword.constraintlayout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.group.*

class GroupActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group)

        //通过 Group 同时为一组 View 设置属性
        buttonGroup.setOnClickListener{
            group.visibility = View.GONE
        }
    }
}