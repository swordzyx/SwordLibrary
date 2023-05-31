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
            clone(container)
        }
        for (i in 1 until mCount) {
            constraintSet.connect(mIds[i - 1], ConstraintSet.BOTTOM, mIds[i], ConstraintSet.TOP)
            constraintSet.connect(mIds[i], ConstraintSet.TOP, mIds[i - 1], ConstraintSet.BOTTOM)
        }
        constraintSet.applyTo(container)
    }
}