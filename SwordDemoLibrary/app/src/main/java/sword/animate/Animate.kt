package sword.animate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

/**
 * 让 targetView 水平从左侧滑出屏幕
 */
fun View.slideOutScreenHorizontalLeft(duration: Long = 800) {
  if(visibility == View.GONE) {
    return
  }
  
  this.animate()
    .translationX(-this.width.toFloat())
    .alpha(0f)
    .setDuration(duration)
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        visibility = View.GONE
      }
    })
}

/**
 * 让 targetView 水平从左侧滑出屏幕
 */
fun View.slideInScreenHorizontalLeft(duration: Long = 800) {
  if (visibility == View.VISIBLE) {
    return
  }
  
  this.animate()
    .translationX(0f)
    .alpha(1f)
    .setDuration(duration)
    .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator) {
        visibility = View.VISIBLE
      }
    })
}