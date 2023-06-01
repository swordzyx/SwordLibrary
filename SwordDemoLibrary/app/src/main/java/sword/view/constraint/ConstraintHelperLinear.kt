package sword.view.constraint

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class ConstraintHelperLinear(context: Context, attrs: AttributeSet) :
    ConstraintHelper(context, attrs) {
    override fun updatePreLayout(container: ConstraintLayout) {
        super.updatePreLayout(container)
        val constraintSet = ConstraintSet().apply {
            isForceId = false
            clone(container)
        }
        for (i in 1 until mCount) {
            constraintSet.connect(mIds[i], ConstraintSet.START, mIds[i - 1], ConstraintSet.START)
            constraintSet.connect(mIds[i], ConstraintSet.END, mIds[i - 1], ConstraintSet.END)
            constraintSet.connect(mIds[i], ConstraintSet.TOP, mIds[i - 1], ConstraintSet.BOTTOM)
        }
        constraintSet.applyTo(container)
    }
}