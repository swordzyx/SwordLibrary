package com.sword.customviewset.scaleimageview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.animation.doOnEnd
import androidx.core.graphics.scale
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.sword.customviewset.R
import com.sword.customviewset.view.dp
import java.lang.Math.max
import java.lang.Math.min

private val IMAGE_SIZE = 300.dp.toInt()
private const val EXTRA_SCALE_FACTOR = 1.5f

class ScaleImageView(context: Context, attrs: AttributeSet): View(context, attrs){
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var big = false
    private val bitmap = getAvatar(IMAGE_SIZE)
    private var bigscale = 0f
    private var smallscale = 0f
    private var originaloffsetX = 0f
    private var originaloffsetY = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var curScale = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val customGestureListener = CustomGestureListener()
    private val refreshRunnable = RefreshRunnable()
    private val scaleDetectorListener = ScaleDetectorListener()
    private val scaleDetector = ScaleGestureDetector(context, scaleDetectorListener)

    private lateinit var animator: ObjectAnimator
    private val gestureDetector =  GestureDetectorCompat(context, customGestureListener)

    //用于自动计算滑动的偏移
    private val scroller = OverScroller(context)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //初始偏移应该在 View 的大小发生改变的时候初始化
        originaloffsetX = (width - bitmap.width) / 2f
        originaloffsetY = (height - bitmap.height) / 2f


        if (bitmap.width/width.toFloat() > bitmap.height/height.toFloat()){
            smallscale = (width / bitmap.width.toFloat())
            bigscale = (height / bitmap.height.toFloat()) * EXTRA_SCALE_FACTOR
        } else {
            smallscale = (height / bitmap.height.toFloat())
            bigscale = (width / bitmap.width.toFloat()) * EXTRA_SCALE_FACTOR
        }
        curScale = smallscale

        animator = ObjectAnimator.ofFloat(this, "curScale", smallscale, bigscale)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        val scaleFraction = (curScale - smallscale) / (bigscale - smallscale)
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction)
        canvas.scale(curScale, curScale, width/2f, height/2f)
        canvas.drawBitmap(bitmap, originaloffsetX, originaloffsetY, paint)
    }

    fun getAvatar(size: Int, resId: Int = R.drawable.avatar_rengwuxian):Bitmap {
        var option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, option)
        option.inJustDecodeBounds = false
        option.inDensity = option.outWidth
        option.inTargetDensity = size
        return BitmapFactory.decodeResource(resources, resId, option)
    }

    private fun fixOffsets() {
        //画布已经被放大了，因此对应的图片宽高也应该乘以放大的比例。
        offsetX = min(offsetX, (bitmap.width * bigscale - width) / 2f)
        offsetX = max(offsetX, - (bitmap.width * bigscale - width) / 2f)
        offsetY = min(offsetY, (bitmap.height * bigscale - height) / 2f)
        offsetY = max(offsetY, -(bitmap.height * bigscale - height) / 2f)
    }

    inner class CustomGestureListener : GestureDetector.SimpleOnGestureListener() {
        //按下时触发，这是会用到的方法
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        //当 View 位于滑动空间中，预按下状态结束时触发
        /*override fun onShowPress(e: MotionEvent?) {
        }*/

        //单击时触发，如果支持长按，那么只有在快速按下并抬起的时候，触发该方法，如果按下 400 ms 之内都没有抬起则会触发长按
        /*override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return false
        }*/

        //滑动时触发，对应 onTouchEvent 中的 move 事件，这里的 distance 是旧的值减去新值的结果
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            if (big) {
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffsets()
                invalidate()
            }
            return false
        }

        //长按时触发
        /*override fun onLongPress(e: MotionEvent?) {
        }*/

        //漂移时触发
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if (big) {
                scroller.fling(offsetX.toInt(), offsetY.toInt(), velocityX.toInt(), velocityY.toInt(), (-(bitmap.width * bigscale - width) / 2).toInt(), ((bitmap.width * bigscale - width) / 2).toInt(), (- (bitmap.height * bigscale - height) / 2).toInt(), ((bitmap.height * bigscale - height) / 2).toInt())
                //postOnAnimation 和 post 都是会将传入的 Runnable 放到主线程中去执行，但是不同的是 post 是马上执行，而 postOnAnimation 则是放到下一帧执行。不过 View 中的 postOnAnimation API 版本比较高，因此需要使用 ViewCompat 中的 postOnAnimation 方法
                ViewCompat.postOnAnimation(this@ScaleImageView, refreshRunnable)
            }
            return false
        }

        //单击触发，这个方法会在确认不是双击之后才会触发，一般是按下抬起之后 400ms 之内如果再没有触发 down 事件，则会认为是单击，也就是说按下抬起，还要再等待 400ms 之后才会触发此方法
        /*override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            return false
        }*/

        //双击时触发
        override fun onDoubleTap(e: MotionEvent): Boolean {
            big = !big
            if (big) {
                offsetX = (e.x - width / 2) * (1 - bigscale/smallscale)
                offsetY = (e.y - height / 2) * (1 - bigscale/smallscale)
                fixOffsets()
                animator.start()
            }else{
                animator.reverse()
            }
            return true
        }

        //双击中第一按下抬起，第二次按下后若触发了其他事件则会触发。
        /*override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            return false
        }*/
    }

    inner class RefreshRunnable: Runnable {
        override fun run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.currX.toFloat()
                offsetY = scroller.currY.toFloat()
                invalidate()
                ViewCompat.postOnAnimation(this@ScaleImageView, this)
            }
        }
    }

    //ScaleGestureDetectorCompat 并不是 ScaleGestureDetector 的兼容版本，它只是一个兼容的辅助工具而已。
    inner class ScaleDetectorListener : ScaleGestureDetector.OnScaleGestureListener {
        //双指在屏幕上缩放时调用
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            //onScale 返回 ture，则scaleFactor是这一次的缩放比例与上一次记录的缩放比例的比值
            val tempScale = detector.scaleFactor
            if (curScale < smallscale || curScale > bigscale) {
                return false
            } else {
                curScale *= detector.scaleFactor

            }
            return true
        }

        //双指放在屏幕上，开始缩放时调用
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            return true
        }
        //结束缩放时调用
        override fun onScaleEnd(detector: ScaleGestureDetector?) {
        }

    }
}