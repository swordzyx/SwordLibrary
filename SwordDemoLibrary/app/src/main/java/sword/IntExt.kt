
package sword

import android.content.res.Resources
import android.util.TypedValue
import sword.logger.SwordLog

private val displayMetrics = Resources.getSystem().displayMetrics

fun dp2px(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)

val Int.dp: Int
    get() = dp2px(this.toFloat()).toInt()

val Float.dp
    get() = dp2px(this)