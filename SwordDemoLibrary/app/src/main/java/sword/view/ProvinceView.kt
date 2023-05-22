package sword.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.sword.dp
import com.sword.dp2px
import kotlin.math.max

private val provinces = listOf(
    "北京市",
    "天津市",
    "上海市",
    "重庆市",
    "河北省",
    "山西省",
    "辽宁省",
    "吉林省",
    "黑龙江省",
    "江苏省",
    "浙江省",
    "安徽省",
    "福建省",
    "江西省",
    "山东省",
    "河南省",
    "湖北省",
    "湖南省",
    "广东省",
    "海南省",
    "四川省",
    "贵州省",
    "云南省",
    "陕西省",
    "甘肃省",
    "青海省",
    "台湾省",
    "内蒙古自治区",
    "广西壮族自治区",
    "西藏自治区",
    "宁夏回族自治区",
    "新疆维吾尔自治区",
    "香港特别行政区",
    "澳门特别行政区"
)

class ProvinceView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = dp2px(16f)
    }
    private val textBounds = Rect()

    private val minWidth = paint.textSize * 10 + dp(10)
    private val minHeight = paint.fontMetrics.bottom - paint.fontMetrics.top + dp(10)

    private val provinceAnimator = ObjectAnimator.ofInt(this, "provinceIndex", 0, provinces.size - 1).apply {
        duration = 3000
        startDelay = 1000
        interpolator = AccelerateInterpolator()
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
    }

    var provinceIndex = 0
        set(value) {
            if (field != value) {
                field = value
                province = provinces[value]
            }
        }

    private var province = "北京市"
        set(value) {
            field = value
            paint.getTextBounds(value, 0, value.length, textBounds)
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = max(MeasureSpec.getSize(widthMeasureSpec), minWidth.toInt() + paddingLeft + paddingRight)
        val height = max(MeasureSpec.getSize(heightMeasureSpec), minHeight.toInt() + paddingTop + paddingBottom)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        paint.getTextBounds(province, 0, province.length, textBounds)
        canvas.drawText(
            province,
            paddingLeft + 0f,
            paddingTop - textBounds.top.toFloat(),
            paint
        )
    }

    fun startAnimator() {
        provinceAnimator.start()

    }
}