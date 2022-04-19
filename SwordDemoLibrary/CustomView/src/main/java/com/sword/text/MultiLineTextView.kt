package com.sword.text

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.example.utilclass.dp
import com.sword.customviewgroup.R

private val IMAGE_WIDTH = 150.dp
private val IMAGE_PADDING = 50.dp

class MultiLineTextView(context: Context?, attr: AttributeSet?): View(context, attr) {

	val text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas non ante ullamcorper, consectetur ante sed, finibus ligula. Cras vitae nunc eget urna pulvinar suscipit et ac tellus. Vestibulum accumsan consectetur dolor. Pellentesque convallis, magna eu tristique sodales, orci massa auctor purus, sed luctus nulla turpis vel massa. In ante nulla, dapibus at posuere ut, dictum vitae mi. Morbi sodales leo at tellus volutpat, in sagittis ligula viverra. Pellentesque pretium, justo vel interdum mattis, augue nulla cursus quam, vitae tristique felis nunc eu arcu. Aenean lobortis tellus vitae turpis porta ullamcorper. In faucibus orci tortor, sit amet mollis mauris blandit sed.\n" +
			"\n" +
			"Mauris semper volutpat enim ac rutrum. Sed pulvinar a magna pellentesque auctor. Vivamus elementum nulla a consequat facilisis. Aliquam tristique posuere lacinia. Nunc pellentesque sapien in massa dictum pretium. In tempor et dui nec rutrum. Duis erat orci, maximus id consectetur a, sagittis a tellus. Maecenas vel semper ex. Donec eu magna scelerisque, pellentesque nunc ut, commodo mauris. Donec pellentesque magna eget blandit ullamcorper. Donec a interdum massa. Phasellus consectetur nec augue id pretium.\n" +
			"\n" +
			"Integer facilisis, risus vitae ultricies rutrum, odio elit pharetra mauris, id condimentum sapien magna ut turpis. Duis ultrices sem id dignissim convallis. Integer vehicula diam nisl, vel scelerisque augue dignissim ut. Sed a bibendum metus, quis luctus dui. Vivamus ac justo in augue ultricies varius. Pellentesque ipsum lacus, congue et risus et, convallis venenatis tellus. Nulla condimentum nulla vel auctor ullamcorper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Donec luctus placerat mi, vitae sollicitudin neque vestibulum sit amet.\n" +
			"\n" +
			"Vestibulum eu imperdiet nisl. Aenean pulvinar, sapien vel dapibus porttitor, urna leo auctor velit, quis luctus ex ante quis tortor. Nam iaculis consectetur libero, vel dignissim libero gravida eget. Curabitur rhoncus, quam id maximus eleifend, neque ex maximus risus, in ultricies urna risus vitae massa. Fusce pellentesque odio non mi consequat, ac porta orci luctus. Integer diam nisl, tristique quis egestas ut, semper ac ante. Vivamus id tincidunt diam, ac blandit nibh. Pellentesque ultrices lacus augue, id porta tellus ornare a. Sed sagittis malesuada lacus, ac auctor sem hendrerit sed.\n" +
			"\n" +
			"Fusce accumsan lectus at dolor aliquam, vehicula faucibus lacus tempus. Mauris enim erat, ultrices sit amet ultricies ut, posuere rhoncus quam. Phasellus sit amet nibh vitae orci pretium aliquet id eget nibh. Morbi nulla ex, dictum ut posuere et, malesuada et tellus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Cras vel facilisis massa. Sed id nisi vitae nisl interdum efficitur eu non nunc. Maecenas venenatis diam quis ligula euismod auctor. Pellentesque luctus volutpat neque a ornare. Fusce justo leo, porta quis sagittis at, suscipit lobortis nulla. Vivamus rhoncus mi lorem, eu consectetur tellus pretium vel. Mauris vel purus tortor."
	private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
		textSize = 16.dp.toFloat()
	}
	private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		textSize = 16.dp.toFloat()
	}

	private val measureTextWidth = floatArrayOf(0f)

	private val staticLayout = StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
	private val bitmap = getBitmap(resources, R.drawable.avatar_rengwuxian, IMAGE_WIDTH)
	//val staticLayout = StaticLayout.Builder.obtain(text, 0, 0, textPaint, width).build()
	private val fontMetrics =

	@SuppressLint("DrawAllocation")
	override fun onDraw(canvas: Canvas) {
		//使用 StaticLayout 显示成段文本
		//staticLayout.draw(canvas)


		canvas.drawBitmap(bitmap, (width - IMAGE_WIDTH).toFloat(), IMAGE_PADDING.toFloat(), paint)

		var textStart = 0
		var yOffset = 0f
		while (textStart < text.length) {
			paint.getFontMetrics(fon)
			val count = if(yOffset >= IMAGE_PADDING && yOffset <= IMAGE_PADDING + IMAGE_WIDTH) {
				paint.breakText(text, 0, text.length, true, (width - IMAGE_WIDTH).toFloat(), measureTextWidth)
			} else {
				paint.breakText(text, 0, text.length, true, width.toFloat(), measureTextWidth)
			}
			//canvas.drawText(String text, int start, int end, float x, float y, Paint paint)
			canvas.drawText(text, textStart, textStart + count, 0f, yOffset, paint)
			yOffset += paint.fontSpacing
			textStart += count
		}
	}

	fun getBitmap(res: Resources, resId: Int, targetWidth: Int, targetHeight: Int = targetWidth): Bitmap {
		val ops = BitmapFactory.Options()
		ops.inJustDecodeBounds = true
		BitmapFactory.decodeResource(res, resId, ops)
		ops.inJustDecodeBounds = false
		ops.inDensity = ops.outWidth
		ops.inTargetDensity = targetWidth.coerceAtMost(targetHeight)
		return BitmapFactory.decodeResource(res, resId, ops)
	}
}