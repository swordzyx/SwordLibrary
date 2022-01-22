package com.sword.customViewDrawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.view.View

class TestView(context: Context, attrs: AttributeSet?) : View(context, attrs){
  
  
  private val RADIUS = 100f.dp
  
  //加 Paint.ANTI_ALIAS_FLAG 参数表示绘制出来的内容会抗锯齿，这个默认是不开启的，因为抗锯齿的原理是修改图形，给图形的边缘做一些模糊化，加一个像素或者减一个像素，让图像看起来更平滑，不过实际上这个图形已经不真实了，而默认是要真实要简单的，要符合代码原本所表示的图形，所以默认是不开的。
  //一般情况下都是要开启抗锯齿的，这样视觉效果会好一些
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  //Path 对象是用来绘制自定义图形的
  private val path = Path()

  /*
  这是用来测量 Path 的，所以这个类的对象应该创建在 Path 初始化完成之后
   */
  lateinit var pathMeasure: PathMeasure
  
  override fun onDraw(canvas: Canvas) {
    //划线，传入起点的 x 和 y，传入终点的 x 和 y，传入一个 Pain 实例，Paint 是用来调整风格的类，例如颜色，所以一共需要传入 5 个参数。
    //从 (100, 100) 从右下画到 (200, 200) 的位置。
    canvas.drawLine(100f, 100f, 200f, 200f, paint )
    
    //画圆。传进来的 RADIUS 单位是像素（px），但我们实际用 xml 写界面的时候都是以 dp 为单位，使用 dp 才能在不同分辨率的设备上实现相同的视觉效果。在绘制之前需要将 dp 转成 px 
    //使用 TypedValue.applyDimension(unit, value, metrics) 可以将任何单位的值都转成 px
    canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)

    //用 Path 画一个圆
    canvas.drawPath(path, paint)


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

    填充规则和图形的方向共同决定了相交的部分是应该填充还是应该留空
     */
    path.addCircle(width / 2f, height / 2f, RADIUS, Path.Direction.CCW)
    //逆时针方向画一个矩形
    path.addRect(width/2f - RADIUS, height/2f, width/2f + RADIUS, height/2f + 2*RADIUS, Path.Direction.CCW)

    /*
    设置填充规则，有以下取值
    FillType.EVEN_ODD：不考虑方向，从一个点向外任意画一条线，只要与图形有相交，就加一。最后的结果，如果是奇数，这个点就在图形的内部，偶数就在图形的外部。如果要做镂空，使用 EVEN_ODD。
    FillType.INVERSE_EVEN_ODD：
    FillType.INVERSE_WINDING：
    FillType.WINDING：这是默认的填充规则。从图形内部一个点向外任意画一条线，如果图形向左插入这条线，就加一，如果向右插入这条线，就减一。最后的值如果小于等于 0 ，这个点就在图形的外部，大于 0 则在图形的外部，这个填充规则用于比较复杂的图形
     */
    path.fillType = Path.FillType.EVEN_ODD

    //第二个参数用于指定是否强制闭合
    pathMeasure = PathMeasure(path, false)
    pathMeasure.length //获取 Path 的长度，如果 path 是一个圆，半径为 r，则 pathMeasure 返回的值就是 2π
    //pathMeasure.getPosTan() //给定一个长度，获取 path 上面对应长度的点的正切值。比如 path 是一个圆，传入 π，则会返回 path 上面距离起点为 π 的点的角度的正切值
  }
}





















