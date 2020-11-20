package com.sword.constraintlayout

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_circular_positioning.*

class CircularPositioning : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circular_positioning)

        val earthAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 10000L
            repeatCount = INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                val params = earth.layoutParams as ConstraintLayout.LayoutParams
                params.circleAngle = 45 + it.animatedFraction * 360
                earth.requestLayout()
            }
        }

        val moonAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000L
            repeatCount = INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                val params = moon.layoutParams as ConstraintLayout.LayoutParams
                params.circleAngle = 270 + it.animatedFraction * 360
                moon.requestLayout()
            }
        }

        sun.setOnClickListener {
            earthAnimator.start()
            moonAnimator.start()
        }
    }
}