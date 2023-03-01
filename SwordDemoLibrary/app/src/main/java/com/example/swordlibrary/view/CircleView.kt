package com.example.swordlibrary.view

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sword.dp
import com.sword.dp2px

class CircleView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val tag = "CircleView"

    /**
     * 半径逐渐变大的动画
     */
    private var objectAnimator: ObjectAnimator? = null
    private var keyframeAnimator: ObjectAnimator? = null

    init {
        //initKeyFrameAnimator()
    }


    var radius = dp2px(20f)
        set(value) {
            field = value
            invalidate()
        }

    //private val bitmap = createBitmap1(resources, R.drawable.avatar_rengwuxian, radius * 2, radius * 2)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#00796B")
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(paddingLeft + radius, paddingTop + radius, radius, paint)
    }

    fun startAnimate() {
        if (keyframeAnimator != null) {
            keyframeAnimator!!.start()
        } else if (objectAnimator != null) {
            objectAnimator!!.start()
        } else {
            animate().translationX(100f).translationY(300f).apply {
                startDelay = 1000
                duration = 1000
            }.start()
        }
    }

    fun stopAnimate() {
        if (objectAnimator != null) {
            if (objectAnimator!!.isRunning && !objectAnimator!!.isPaused) {
                objectAnimator!!.pause()
            }
        }
    }

    private fun initKeyFrameAnimator() {
        val length = dp(200)
        val keyFrame1 = Keyframe.ofFloat(0f, 0f)
        val keyFrame2 = Keyframe.ofFloat(0.2f, 0.4f * length)
        val keyFrame3 = Keyframe.ofFloat(0.8f, 0.6f * length)
        val keyFrame4 = Keyframe.ofFloat(1f, 1f * length)
        val keyframeHolderY = PropertyValuesHolder.ofKeyframe(
            "translationX",
            keyFrame1,
            keyFrame2,
            keyFrame3,
            keyFrame4
        )
        val keyframeHolder = PropertyValuesHolder.ofKeyframe(
            "translationY",
            keyFrame1,
            keyFrame2,
            keyFrame3,
            keyFrame4
        )
        keyframeAnimator =
            ObjectAnimator.ofPropertyValuesHolder(this, keyframeHolder, keyframeHolderY)
        keyframeAnimator!!.duration = 2000
        keyframeAnimator!!.startDelay = 1000
    }

    private fun initObjectAnimator() {
        objectAnimator = ObjectAnimator.ofFloat(this, "radius", dp2px(100f), dp2px(150f)).apply {
            duration = 2000
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }
    }
}