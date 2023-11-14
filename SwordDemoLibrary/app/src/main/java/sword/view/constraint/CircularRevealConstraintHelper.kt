package sword.view.constraint

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import sword.logger.SwordLog
import kotlin.math.hypot

/**
 * 布局加载完成之后，给 CircularRevealConstraintHelper 所包含的所有的 view 添加一个裁剪圆动画，视觉效果就是从左上角为圆心的逐渐显示 view
 */
class CircularRevealConstraintHelper(context: Context, attrs: AttributeSet) :
    ConstraintHelper(context, attrs) {
    private val tag = "CircularRevealConstraintHelper"

    /**
     * 布局加载完成时回调
     */
    override fun updatePostLayout(container: ConstraintLayout) {
        super.updatePostLayout(container)
        SwordLog.debug(tag, "updatePostLayout，布局加载之后回调")

        referencedIds.forEach { id ->
            val view = container.findViewById<View>(id)
            val radius = hypot(view.width.toDouble(), view.height.toDouble())

            ViewAnimationUtils.createCircularReveal(view, 0, 0, 0f, radius.toFloat())
                .setDuration(2000)
                .start()
        }
    }

    override fun updatePreLayout(container: ConstraintLayout) {
        super.updatePreLayout(container)
        SwordLog.debug(tag, "updatePreLayout，布局加载之前回调")
    }

    override fun updatePostConstraints(container: ConstraintLayout) {
        super.updatePostConstraints(container)
        SwordLog.debug(tag, "updatePostConstraints")
    }

    override fun updatePostMeasure(container: ConstraintLayout?) {
        super.updatePostMeasure(container)
        SwordLog.debug(tag, "updatePostMeasure")
    }
}