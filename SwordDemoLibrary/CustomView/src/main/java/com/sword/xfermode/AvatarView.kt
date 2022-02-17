package com.sword.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sword.customViewDrawing.dp
import com.sword.customviewgroup.R

private val IMAGE_WIDTH = 200f.dp
private val IMAGE_PADDING = 20f.dp
private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

class AvatarView(context: Context, attrs: AttributeSet) : View(context, attrs) {
	private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private val bounds = RectF(IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING + IMAGE_WIDTH , IMAGE_PADDING + IMAGE_WIDTH)

	override fun onDraw(canvas: Canvas) {
		val count = canvas.saveLayer(bounds, null)
		//绘制圆
		canvas.drawOval(IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING + IMAGE_WIDTH, IMAGE_PADDING + IMAGE_WIDTH, paint)
		//绘制图片
		paint.xfermode = XFERMODE
		canvas.drawBitmap(getAvatar(), IMAGE_PADDING, IMAGE_PADDING, paint)
		paint.xfermode = null
		canvas.restoreToCount(count)
	}


	private fun getAvatar(): Bitmap {
		//Bitmap 优化，做两次图片读取，第一次仅读取图片的尺寸信息，这会非常的快，第二次先按照需要配置好尺寸信息，读取出的图片就是我们需要的尺寸的图片了
		val ops = BitmapFactory.Options()
		ops.inJustDecodeBounds = true
		BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, ops)
		ops.inDensity = ops.outWidth
		ops.inJustDecodeBounds = false
		ops.inTargetDensity = IMAGE_WIDTH.toInt()
		return BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, ops)
	}
}