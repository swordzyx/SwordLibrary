package com.sword.customviewset.view.xfermode

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.sword.customviewset.R
import com.sword.customviewset.view.px

class AvatarView(context: Context, attrs: AttributeSet) : View(context, attrs){
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val PADDING = 20f.px
    private val IMAGE_WIDTH = 200f.px
    private var bounds: RectF
    private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private lateinit var avatar : Bitmap
    private var offset: Float = 0f

    init {
        bounds = RectF(PADDING, PADDING, PADDING + IMAGE_WIDTH, PADDING + IMAGE_WIDTH)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        avatar = getAvatar(IMAGE_WIDTH)
        offset = (avatar.width - IMAGE_WIDTH) / 2
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        val layer = canvas?.saveLayer(bounds, null)
        canvas?.drawOval(bounds, paint)
        paint.xfermode = XFERMODE
        canvas?.drawBitmap(avatar, PADDING - offset, PADDING, paint)
        paint.xfermode = null
        canvas?.restoreToCount(layer!!)
    }

    private fun getAvatar(width: Float, resId: Int = R.mipmap.psb): Bitmap {
        val options = BitmapFactory.Options()
        //获取图片原始宽高
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)

        //将图片定义成合适的宽高
        options.inJustDecodeBounds = false
        options.inDensity = options.outHeight
        options.inTargetDensity = width.toInt()

        return BitmapFactory.decodeResource(resources, resId, options)
    }
}


