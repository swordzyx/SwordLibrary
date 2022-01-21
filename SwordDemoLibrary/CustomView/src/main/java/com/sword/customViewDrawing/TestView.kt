package com.sword.customViewDrawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class TestView(context: Context, attrs: AttributeSet?) : View(context, attrs){
  
  
  private val RADIUS = 200f 
  
  //加 Paint.ANTI_ALIAS_FLAG 参数表示绘制出来的内容会抗锯齿，这个默认是不开启的，因为抗锯齿的原理是修改图形，给图形的边缘做一些模糊化，加一个像素或者减一个像素，让图像看起来更平滑，不过实际上这个图形已经不真实了，而默认是要真实要简单的，要符合代码原本所表示的图形，所以默认是不开的。
  //一般情况下都是要开启抗锯齿的，这样视觉效果会好一些
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val path = Path()
  
  override fun onDraw(canvas: Canvas) {
    //划线，传入起点的 x 和 y，传入终点的 x 和 y，传入一个 Pain 实例，Paint 是用来调整风格的类，例如颜色，所以一共需要传入 5 个参数。
    //从 (100, 100) 从右下画到 (200, 200) 的位置。
    canvas.drawLine(100f, 100f, 200f, 200f, paint )
    
    //画圆。传进来的 RADIUS 单位是像素（px），但我们实际用 xml 写界面的时候都是以 dp 为单位，使用 dp 才能在不同分辨率的设备上实现相同的视觉效果。在绘制之前需要将 dp 转成 px 
    //使用 TypedValue.applyDimension(unit, value, metrics) 可以将任何单位的值都转成 px
    canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)
  }

  //View 的尺寸发生改变的时候，此方法会被调用
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    /*
    View 尺寸发生改变的时候，View 里面的内容也会随之发送变化，在这里对 Path 进行初始化
     */
    path.reset()
    /*
    Path.Direction.CW: clock wise，顺时针方向
    Path.Direction.CCW: Counter-Clock Wise，逆时针方向
    在绘制单个图形的时候，顺时针或者逆时针的效果是一样的。Path 的方向主要作用于多个图形相交时，相交部分的内容是否填充（相交部分是属于图形的内部还是图形的外部）
     */
    path.addCircle(width / 2f, height / 2f, RADIUS, Path.Direction.CW)
  }
}