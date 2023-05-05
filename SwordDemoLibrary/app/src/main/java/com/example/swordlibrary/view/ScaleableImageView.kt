package com.example.swordlibrary.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector.OnDoubleTapListener
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import com.example.swordlibrary.R
import com.sword.LogUtil
import com.sword.createBitmap
import com.sword.dp
import kotlin.math.max
import kotlin.math.min

class ScaleableImageView(context: Context, attributeSet: AttributeSet) :
  View(context, attributeSet), Runnable {
  private val tag = "ScaleableImageView"
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val IMAGE_SIZE = dp(300)
  private val SCALE_FACTOR = 3
  private val bitmap = createBitmap(
    context.resources,
    R.drawable.avatar_rengwuxian,
    IMAGE_SIZE.toFloat(),
    IMAGE_SIZE.toFloat()
  )
  private var originalOffsetX = 0f
  private var originalOffsetY = 0f
  private var offsetX = 0f
  private var offsetY = 0f

  private var big = false

  private var bigScale = 0f
  private var smallScale = 0f
  private var currentScale = 0f
    set(value) {
      field = value
      invalidate()
    }
  private var currentFraction = 0f
    set(value) {
      field = value
      invalidate()
    }

  private val fractionAnimator by lazy {
    ObjectAnimator.ofFloat(this, "currentFraction", 0f, 1f)
  }

  private val scaleAnimator by lazy {
    ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale)
  }

  private val overScroller = OverScroller(context)
  private val scaleGestureListener = object : ScaleGestureDetector.OnScaleGestureListener {

    //捏撑
    override fun onScale(detector: ScaleGestureDetector): Boolean {
      //通过 detector.scaleFactor 可以获取到实时的缩放系数，当前状态和上一个状态的比值
      currentScale *= detector.scaleFactor
      currentScale = max(smallScale, currentScale)
      currentScale = min(bigScale, currentScale)
      return true
    }

    //开始捏撑
    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
      offsetX = (detector.focusX - width / 2) * (1 - bigScale / smallScale)
      offsetY = (detector.focusY - height / 2) * (1 - bigScale / smallScale)
      limitEdge(offsetX, offsetY)
      return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
    }

  }

  private fun limitEdge(offsetX: Float, offsetY: Float) {
    val w = (bitmap.width * bigScale - width) / 2f
    val h = (bitmap.height * bigScale - height) / 2f
    this.offsetX = min(w, offsetX)
    this.offsetX = max(-w, offsetX)
    this.offsetY = min(h, offsetY)
    this.offsetY = max(-h, offsetY)
  }

  //手指捏撑
  private val scaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)

  //onDoubleTapListener 和 OnGestureListener 是同一个对象时，会自动调用 addDoubleTapListener
  private val onGestureListener = object : SimpleOnGestureListener(), OnDoubleTapListener {
    //down 事件
    override fun onDown(e: MotionEvent): Boolean {
      return true
    }

    //双击事件，第一次按下之后，300ms 之内按下第二次，触发此函数
    override fun onDoubleTap(e: MotionEvent): Boolean {
      big != big
      if (big) {
        scaleAnimator.start()
        offsetX = (e.x - width / 2) * (1 - bigScale / smallScale)
        offsetY = (e.y - height / 2) * (1 - bigScale / smallScale)
        limitEdge(offsetX, offsetY)
      } else {
        scaleAnimator.reverse()
      }
      return true
    }

    //distanceX 和 distanceY 是这次事件距离上次事件的距离
    override fun onScroll(
      e1: MotionEvent,
      e2: MotionEvent,
      distanceX: Float,
      distanceY: Float
    ): Boolean {
      LogUtil.debug(tag, "onScroll distanceX: $distanceX, distanceY: $distanceY")
      if (big) {
        offsetX += distanceX
        offsetY += distanceY

        limitEdge(offsetX, offsetY)
        invalidate()
      }
      return true
    }

    override fun onFling(
      e1: MotionEvent,
      e2: MotionEvent,
      velocityX: Float,
      velocityY: Float
    ): Boolean {
      if (!big) {
        return false
      }
      val x = ((bitmap.width * bigScale - width) / 2).toInt()
      val y = ((bitmap.height * bigScale - height) / 2).toInt()
      overScroller.fling(
        e2.x.toInt(),
        e2.y.toInt(),
        velocityX.toInt(),
        velocityY.toInt(),
        -x,
        x,
        -y,
        y
      )
      postOnAnimation(this@ScaleableImageView)
      return true
    }
  }

  private val gestureDetector = GestureDetectorCompat(context, onGestureListener)


  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    //手势
    if (gestureDetector.onTouchEvent(event)) {
      return true
    }
    //捏撑
    if (scaleGestureDetector.onTouchEvent(event)) {
      return true
    }
    return super.onTouchEvent(event)
  }


  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    originalOffsetX = (w - IMAGE_SIZE) / 2f
    originalOffsetY = (h - IMAGE_SIZE) / 2f

    //计算外贴边图和内贴边图的缩放比例
    if (bitmap.width / bitmap.height < width / height) {
      smallScale = height / bitmap.height.toFloat()
      bigScale = width / bitmap.width.toFloat() * SCALE_FACTOR
    } else {
      smallScale = width / bitmap.width.toFloat()
      bigScale = height / bitmap.height.toFloat() * SCALE_FACTOR
    }
  }

  override fun onDraw(canvas: Canvas) {
    val fraction = (currentScale - smallScale) / (bigScale - smallScale)
    canvas.translate(offsetX * fraction, offsetY * fraction)
    //缩放
    canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
    //居中绘制图片
    canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
  }

  //松开手指之后图片的自由滑动
  override fun run() {
    //computeScrollOffset 返回 true，表示 fling 滑动还未结束
    if (overScroller.computeScrollOffset()) {
      offsetX = overScroller.currX.toFloat()
      offsetY = overScroller.currY.toFloat()
      invalidate()
      //使用匿名内部类对象无法通过 this 定位当前 Runnable
      postOnAnimation(this)
    }
  }
}