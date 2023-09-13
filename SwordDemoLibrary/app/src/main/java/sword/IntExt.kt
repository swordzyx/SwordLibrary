package sword

import android.content.res.Resources
import android.util.TypedValue

private val displayMetrics = Resources.getSystem().displayMetrics

fun dp2px(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)

val Int.dp
    get() = dp2px(this.toFloat()).toInt()