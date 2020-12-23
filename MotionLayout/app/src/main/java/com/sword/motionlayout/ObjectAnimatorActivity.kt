package com.sword.motionlayout

import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_object_animator.*
import java.security.acl.Group

class ObjectAnimatorActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_animator)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun onClick(v: View) {
        TransitionManager.beginDelayedTransition(root)

        with(v.layoutParams as FrameLayout.LayoutParams) {
            gravity = Gravity.CENTER
            height *= 2
            width *=2
        }

        v.requestLayout()
    }
}