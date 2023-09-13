package sword.view

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
import androidx.core.animation.doOnEnd
import androidx.core.view.GestureDetectorCompat
import com.example.swordlibrary.R
import sword.SwordLog
import sword.createBitmap1
import sword.dp

class ScaleableImageView(context: Context, attributeSet: AttributeSet? = null) :
  View(context, attributeSet), Runnable {
  private val tag = "ScaleableImageView"
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val IMAGE_SIZE = dp(300)
  private val SCALE_FACTOR = 2
  private val bitmap = createBitmap1(
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
  private var animatorStartValue = smallScale
  private var animatorEndValue = bigScale

  private val scaleAnimator by lazy {
    ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale).apply {
      doOnEnd {
        if (animatorStartValue != smallScale || animatorEndValue != bigScale) {
          this.setFloatValues(smallScale, bigScale)
          animatorStartValue = smallScale
          animatorEndValue = bigScale
        }
      }
    }

  }

  private val overScroller = OverScroller(context)
  private val scaleGestureListener = object : ScaleGestureDetector.OnScaleGestureListener {
    //捏撑
    override fun onScale(detector: ScaleGestureDetector): Boolean {
      //通过 detector.scaleFactor 可以获取到实时的缩放系数，当前状态和上一个状态的比值
      val tempScale = currentScale * detector.scaleFactor
      SwordLog.debug(tag, "onScale tempScale: $tempScale")
      if (tempScale < smallScale) {
        if (currentScale != smallScale) {
          currentScale = smallScale
        }
        return false
      }

      if (tempScale > bigScale) {
        if (currentScale != bigScale) {
          currentScale = bigScale
        }
        return false
      }
      
      if(currentScale != tempScale) {
        currentScale = tempScale
      }
      return true
    }

    //开始捏撑
    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
      offsetX = (detector.focusX - width / 2) * (1 - animatorEndValue / animatorStartValue)
      offsetY = (detector.focusY - height / 2) * (1 - animatorEndValue / animatorStartValue)
      SwordLog.debug(tag, "onScaleBegin, offsetX: $offsetX, offsetY: $offsetY")
      limitEdge(offsetX, offsetY)
      return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
      if (currentScale == bigScale || currentScale == smallScale) {
        if (currentScale == bigScale) {
          big = true
        } else if (currentScale == smallScale) {
          big = false
        }
        animatorStartValue = smallScale
        animatorEndValue = bigScale
      } else {
        if (big) {
          animatorStartValue = smallScale
          animatorEndValue = currentScale
        } else {
          animatorStartValue = currentScale
          animatorEndValue = bigScale
        }
      }
      scaleAnimator.setFloatValues(animatorStartValue, animatorEndValue)
      SwordLog.debug(
        tag,
        "onScaleEnd, offsetX: $offsetX, offsetY: $offsetY, animatorEndValue: $animatorEndValue, animatorStartValue: $animatorStartValue"
      )
    }

  }

  private fun limitEdge(offsetX: Float, offsetY: Float) {
    val w = (bitmap.width * bigScale - width) / 2f
    val h = (bitmap.height * bigScale - height) / 2f
    this.offsetX = offsetX.coerceAtLeast(-w).coerceAtMost(w)
    this.offsetY = offsetY.coerceAtLeast(-h).coerceAtMost(h)
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
      big = !big
      if (big) {
        offsetX = (e.x - width / 2) * (1 - bigScale / currentScale)
        offsetY = (e.y - height / 2) * (1 - bigScale / currentScale)
        limitEdge(offsetX, offsetY)
        scaleAnimator.start()
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
      SwordLog.debug(tag, "onScroll distanceX: $distanceX, distanceY: $distanceY")
      if (big) {
        offsetX -= distanceX
        offsetY -= distanceY

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
      overScroller.fling(
        offsetX.toInt(),
        offsetY.toInt(),
        velocityX.toInt(),
        velocityY.toInt(),
        (-(bitmap.width * bigScale - width) / 2).toInt(),
        ((bitmap.width * bigScale - width) / 2).toInt(),
        (-(bitmap.height * bigScale - height) / 2).toInt(),
        ((bitmap.height * bigScale - height) / 2).toInt()
      )
      postOnAnimation(this@ScaleableImageView)
      SwordLog.debug(tag, "onFling velocityX: $velocityX, velocitY: $velocityY")
      return true
    }

  }

  private val gestureDetector = GestureDetectorCompat(context, onGestureListener)


  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    //捏撑
    scaleGestureDetector.onTouchEvent(event)
    //手势
    if (!scaleGestureDetector.isInProgress) {
      gestureDetector.onTouchEvent(event)
    }
    return true
  }


  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    originalOffsetX = (w - IMAGE_SIZE) / 2f
    originalOffsetY = (h - IMAGE_SIZE) / 2f

    //计算外贴边图和内贴边图的缩放比例
    if ((bitmap.width / bitmap.height) < (w / h)) {
      SwordLog.debug(tag, "瘦图")
      smallScale = h / bitmap.height.toFloat()
      bigScale = w / bitmap.width.toFloat() * SCALE_FACTOR
    } else {
      SwordLog.debug(tag, "胖图")
      smallScale = w / bitmap.width.toFloat()
      bigScale = h / bitmap.height.toFloat() * SCALE_FACTOR
    }
    currentScale = smallScale
    animatorStartValue = smallScale
    animatorEndValue = bigScale
    scaleAnimator.setFloatValues(smallScale, bigScale)
    SwordLog.debug(
      tag,
      "onSizeChange, bitmap width: ${bitmap.width} , height: ${bitmap.height}, bigScale $bigScale, smallScale: $smallScale"
    )
  }

  override fun onDraw(canvas: Canvas) {
    val fraction = (currentScale - animatorStartValue) / (animatorEndValue - animatorStartValue)
    canvas.translate(offsetX * fraction, offsetY * fraction)

    //缩放
    canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
    //居中绘制图片
    canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    SwordLog.debug(
      tag,
      "onDraw smallScale: $smallScale, bigScale: $bigScale, currentScale: $currentScale, animatorStartValue: $animatorStartValue, animatorEndValue: $animatorEndValue, offsetX: $offsetX, offsetY: $offsetY, fraction: $fraction"
    )
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
    SwordLog.debug(tag, "onFling run, offsetX: $offsetX, offsetY: $offsetY")
  }
}