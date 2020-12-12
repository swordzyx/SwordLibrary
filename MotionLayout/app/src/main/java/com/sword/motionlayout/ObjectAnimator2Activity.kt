package com.sword.motionlayout

import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class ObjectAnimator2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_animator2)

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun onClick(v: View) {
        TransitionManager.beginDelayedTransition(v.parent as ViewGroup)

        with(v.layoutParams as LinearLayout.LayoutParams) {
            width *= 2
            height *= 2
        }

        v.requestLayout()
    }
}