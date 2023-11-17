package sword.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import sword.dp
import sword.dp2px
import kotlin.random.Random

private val COLORS = intArrayOf(
    Color.parseColor("#E91E63"),
    Color.parseColor("#673AB7"),
    Color.parseColor("#3F51B5"),
    Color.parseColor("#2196F3"),
    Color.parseColor("#009688"),
    Color.parseColor("#FF9800"),
    Color.parseColor("#FF5722"),
    Color.parseColor("#795548")
)


class ColorTextView(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs) {
    private val textSizes = floatArrayOf(12f, 16f, 20f)
    private val paddingHori = 16.dp
    private val paddingVert = 18.dp
    private val cornerRadius = dp2px(5f)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = COLORS[Random.nextInt(COLORS.size)]
    }

    init {
        setPadding(
            paddingLeft + paddingHori,
            paddingTop + paddingVert,
            paddingRight + paddingHori,
            paddingBottom + paddingVert
        )
        textSize = textSizes[Random.nextInt(textSizes.size)]
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(
            0f,
            0f,
            paddingLeft + width + paddingHori * 2f,
            paddingTop + height + paddingVert * 2f,
            cornerRadius,
            cornerRadius,
            paint
        )
        super.onDraw(canvas)
    }
}