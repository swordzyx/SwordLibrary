package com.sword.customviewset

import android.animation.*
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.sword.customviewset.view.ProvinceEvaluator

class AnimatorActivity : AppCompatActivity() {
    lateinit var view: View

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animator)

        view = findViewById(R.id.view)

        /*view.animate()
                .translationX(200.dp)
                .translationY(100.dp)
                .alpha(0.5f)
                .scaleX(2f)
                .scaleY(2f)
                .rotation(90f)
                .setStartDelay(1000)*/

        /*val animator = ObjectAnimator.ofFloat(view, "radius", 150.dp)
        animator.startDelay = 1000
        animator.start()*/

        /*val bottomRotationAnimator = ObjectAnimator.ofFloat(view, "bottomRotation", 60f)
        bottomRotationAnimator.startDelay = 1000
        bottomRotationAnimator.duration = 1000

        val topRotationAnimator = ObjectAnimator.ofFloat(view, "topRotation", -60f)
        topRotationAnimator.startDelay = 200
        topRotationAnimator.duration = 1000

        val dividerRotationAnimator = ObjectAnimator.ofFloat(view, "divideRotation", 270f)
        dividerRotationAnimator.startDelay = 200
        dividerRotationAnimator.duration = 1000

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(bottomRotationAnimator, dividerRotationAnimator, topRotationAnimator )
        animatorSet.start()*/

        /*val valueHolder1 = PropertyValuesHolder.ofFloat("bottomRotation", 60f)
        val valueHolder2 = PropertyValuesHolder.ofFloat("divideRotation", 270f)
        val valueHolder3 = PropertyValuesHolder.ofFloat("topRotation", -60f)

        val holderAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valueHolder1, valueHolder2, valueHolder3)
        holderAnimator.startDelay = 1000
        holderAnimator.duration = 2000
        holderAnimator.start()*/

        /*val length = 200.dp
        val keyframe1 = Keyframe.ofFloat(0f, 0f)
        val keyframe2 = Keyframe.ofFloat(0.2f, 0.4f*length)
        val keyframe3 = Keyframe.ofFloat(0.8f, 0.6f*length)
        val keyframe4 = Keyframe.ofFloat(1.0f, length)

        val keyframeHolder = PropertyValuesHolder.ofKeyframe("radius", keyframe1, keyframe2, keyframe3, keyframe4)
        val keyframeAnimator = ObjectAnimator.ofPropertyValuesHolder(view, keyframeHolder)
        keyframeAnimator.startDelay = 1000
        keyframeAnimator.duration = 2000
        keyframeAnimator.start()*/

        /*val animator = ObjectAnimator.ofFloat(view, "radius", 200.dp)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.startDelay = 1000
        animator.duration = 1000
        animator.start()*/

        /*val animator = ObjectAnimator.ofObject(view, "point", PointFEvaluator(), PointF(100.dp, 200.dp))
        animator.startDelay = 1000
        animator.duration = 1000
        animator.start()*/

        val animator = ObjectAnimator.ofObject(view, "province", ProvinceEvaluator(), "澳门特别行政区")
        animator.startDelay = 1000
        animator.duration = 10000
        animator.start()
    }
}