package com.sword.customviewset.scaleimageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.scale
import androidx.core.view.GestureDetectorCompat
import com.sword.customviewset.R
import com.sword.customviewset.view.dp

private val IMAGE_SIZE = 300.dp.toInt()

class ScaleImageView(context: Context, attrs: AttributeSet): View(context, attrs), GestureDetector.OnGestureListener {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var big = false
    private val bitmap = getAvatar(IMAGE_SIZE)
    private var bigscale = 0f
    private var smallscale = 0f
    private var originaloffsetX = 0f
    private var originaloffsetY = 0f
    private val gestureDetector =  GestureDetectorCompat(context, this)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //初始偏移应该在 View 的大小发生改变的时候初始化
        originaloffsetX = (width - bitmap.width) / 2f
        originaloffsetY = (height - bitmap.height) / 2f

        if (bitmap.width/width.toFloat() > bitmap.height/height.toFloat()){
            smallscale = (width / bitmap.width.toFloat())
            bigscale = (height / bitmap.height.toFloat())
        } else {
            smallscale = (height / bitmap.height.toFloat())
            bigscale = (width / bitmap.width.toFloat())
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.scale(bigscale, bigscale, width/2f, height/2f)
        canvas.drawBitmap(bitmap, originaloffsetX, originaloffsetY, paint)
    }

    fun getAvatar(size: Int, resId: Int = R.drawable.avatar_rengwuxian):Bitmap {
        var option = BitmapFactory.Options();
        option.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, option)
        option.inJustDecodeBounds = false
        option.inDensity = option.outWidth
        option.inTargetDensity = size
        return BitmapFactory.decodeResource(resources, resId, option)
    }

    //按下时触发，这是会用到的方法
    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    //当 View 位于滑动空间中，预按下状态结束时触发
    override fun onShowPress(e: MotionEvent?) {
    }

    //单击时触发，如果支持长按，那么只有在快速按下并抬起的时候，触发该方法，如果按下 400 ms 之内都没有抬起则会触发长按
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    //滑动时触发，对应 onTouchEvent 中的 move 事件
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {

    }

    //长按时触发
    override fun onLongPress(e: MotionEvent?) {
    }

    //漂移时触发
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }
}