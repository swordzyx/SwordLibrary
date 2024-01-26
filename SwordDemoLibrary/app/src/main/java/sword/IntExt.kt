
package sword

import android.content.res.Resources
import android.util.TypedValue
import java.text.DecimalFormat

private val displayMetrics = Resources.getSystem().displayMetrics

fun dp2px(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)

val Int.dp: Int
    get() = dp2px(this.toFloat()).toInt()

val Float.dp
    get() = dp2px(this)

/**
 * 1 度对应的弧度
 */
const val PI_DIV_180 = Math.PI / 180f

/**
 * 计算弧长对应的角度
 */
fun arcToAngle(arcLength: Int, radius: Float): Float {
    return (arcLength.toFloat() / PI_DIV_180 * radius).toFloat()
}

/**
 * 角度转弧度
 */
fun angleToRadian(angle: Float): Float {
    return (angle * PI_DIV_180).toFloat()
}

