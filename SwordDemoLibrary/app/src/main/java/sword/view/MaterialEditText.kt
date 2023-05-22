package sword.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.swordlibrary.R
import sword.utils.Theme
import com.sword.LogUtil
import com.sword.dp2px

class MaterialEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    private val tag = "MaterialEditText"

    private val labelMarginBottom = dp2px(10f)
    private val labelTextColor = sword.utils.Theme.getColor(context, R.color.colorAccent)
    private val labelTextSize = dp2px(10f)
    private val labelAnimateOffset = dp2px(20f)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = labelTextColor
        textSize = labelTextSize
    }

    private var enableFloatLabel = false
        set(value) {
            if (field != value) {
                field = value
                if (value) {
                    setPadding(
                        paddingLeft,
                        (paddingTop + labelTextSize + labelMarginBottom + 1).toInt(),
                        paddingRight,
                        paddingBottom
                    )
                } else {
                    setPadding(
                        paddingLeft,
                        (paddingTop - labelTextSize - labelMarginBottom - 1).toInt(),
                        paddingRight,
                        paddingBottom
                    )
                }
            }
        }

    private val floatLabelAnimator by lazy {
        ObjectAnimator.ofFloat(this, "floatLabelAnimateFraction", 0f, 1f).apply {
            duration = 100
        }
    }

    var showFloatLabel = false
        set(value) {
            if (enableFloatLabel && value != field) {
                if (value) {
                    floatLabelAnimator.start()
                } else {
                    floatLabelAnimator.reverse()
                }
            }
            field = value
        }

    private var floatLabelAnimateFraction = 0f
        set(value) {
            field = value
            if (enableFloatLabel) {
                invalidate()
            }
        }

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s) && showFloatLabel) {
                    showFloatLabel = false
                } else if (!TextUtils.isEmpty(s) && !showFloatLabel) {
                    showFloatLabel = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        //打印所有的属性
        LogUtil.debug(tag, "attributeCount: ${attrs.attributeCount}")
        for (i in 0 until attrs.attributeCount) {
            LogUtil.debug(tag, "key: ${attrs.getAttributeName(i)}, value: ${attrs.getAttributeValue(i)}")
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText) //拿到 R.txt 中 int[] styleable MaterialEditText { 0x7f0403f8 }

        LogUtil.debug(tag, "enableFloatLabel index: ${typedArray.getIndex(R.styleable.MaterialEditText_enableFloatLabel)}")
        //typedArray.getIndex(R.styleable.MaterialEditText_enableFloatLabel) 拿到 int styleable MaterialEditText_enableFloatLabel 0，即 attrs 中 MaterialEditText 这个 styleable 里面声明的第一个属性值
        //下面这一行代码等价于 typedArray.getBoolean(0, true)
        enableFloatLabel = typedArray.getBoolean(typedArray.getIndex(R.styleable.MaterialEditText_enableFloatLabel), true)
        typedArray.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (enableFloatLabel) {
            paint.alpha = (floatLabelAnimateFraction * 0xff).toInt()
            canvas.drawText(
                hint as String,
                paddingLeft.toFloat(),
                labelTextSize + labelMarginBottom + labelAnimateOffset * (1 - floatLabelAnimateFraction),
                paint
            )
        }
    }
}