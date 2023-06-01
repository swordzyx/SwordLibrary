package sword.view.constraint

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import com.example.swordlibrary.R
import com.sword.LogUtil
import com.sword.dp
import sword.utils.createButtonToShowViewInContainer
import sword.view.INavigationLayout

class ConstraintLayoutSampleContainer(context: Context, attributeSet: AttributeSet? = null) :
  LinearLayout(context, attributeSet), INavigationLayout {
  private val tag = "ConstraintLayoutSampleContainer"
  private val container = FrameLayout(context).apply {
    visibility = View.GONE
  }
  //需要放在初始化代码块之前，否则 init 代码块中使用这个变量时，变量还未初始化，得到的值是 0
  private val defaultMargin = dp(2)
  
  init {
    orientation = VERTICAL
    addView(container, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    addBaseAttrSample()
    addCircularRevealConstraintHelperSample()
    addConstraintSetSample()
    addConstraintSetLinearSample()
  }

  private var baseAttrLayout: ConstraintLayout? = null
  @SuppressLint("InflateParams")
  private fun addBaseAttrSample() {
    if (baseAttrLayout == null) {
      baseAttrLayout = LayoutInflater.from(context).inflate(R.layout.constraint_base_attr_sample, null) as ConstraintLayout
      addView(
        createButtonToShowViewInContainer(
          "约束布局基础属性示例",
          baseAttrLayout!!,
          container
        ),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
    
    if (baseAttrLayout?.visibility != View.VISIBLE) {
      baseAttrLayout?.visibility = View.VISIBLE
    }
  }

  private var circularRevealConstraintHelperSample: ConstraintLayout? = null
  @SuppressLint("InflateParams")
  private fun addCircularRevealConstraintHelperSample() {
    if (circularRevealConstraintHelperSample == null) {
      circularRevealConstraintHelperSample =
        LayoutInflater.from(context).inflate(R.layout.circular_reveal_constraint_helper, null) as ConstraintLayout
      addView(
        createButtonToShowViewInContainer(
          "ConstraintHelper 示例",
          circularRevealConstraintHelperSample!!,
          container
        ),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
    
    if (circularRevealConstraintHelperSample?.visibility != View.VISIBLE) {
      circularRevealConstraintHelperSample?.visibility = View.VISIBLE
    }
  }

  /**
   * ConstraintSet 示例一，动态为 R.id.twitter 的 View 添加一个 app:layout_constraintBottom_toBottomOf=“parent" 的属性
   */
  private var constraintSetSample1: ConstraintLayout? = null
  @SuppressLint("InflateParams")
  private fun addConstraintSetSample() {
    if (constraintSetSample1 == null) {
      constraintSetSample1 = LayoutInflater.from(context)
        .inflate(R.layout.constraint_set_sample_1, null) as ConstraintLayout
      constraintSetSample1!!.setOnClickListener {
        val constraintSet = ConstraintSet().apply {
          isForceId = false
          //将 constraintLayout 中的约束拷贝到 ConstraintSet 中
          clone(constraintSetSample1)
          connect(
            R.id.twitter,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
          )
        }
        constraintSet.applyTo(constraintSetSample1)
      }
      addView(
        createButtonToShowViewInContainer(
          "ConstraintSet 示例，动态改变 View 的约束",
          constraintSetSample1!!,
          container
        ),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
    
    if (constraintSetSample1?.visibility != View.VISIBLE) {
      constraintSetSample1?.visibility = View.VISIBLE
    }
  }

  /**
   * ConstraintSet 示例二, 自定义一个 ConstraintHelper，实现对其所引用的 View 的垂直线性排列
   */
  private var constraintSetLinear: ConstraintLayout? = null
  @SuppressLint("InflateParams")
  private fun addConstraintSetLinearSample() {
    if (constraintSetLinear == null) {
      constraintSetLinear = LayoutInflater.from(context).inflate(R.layout.constraint_set_linear, null) as ConstraintLayout
      addView(createButtonToShowViewInContainer(
        "ConstraintSet 示例，结合 ConstraintHelper 实现线性布局",
        constraintSetLinear!!,
        container),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
    
    if (constraintSetLinear?.visibility != View.VISIBLE) {
      constraintSetLinear?.visibility = View.VISIBLE
    }
    
    LogUtil.debug(tag, "addConstraintSetLinearSample defaultVerticalMargin: $defaultMargin")
  }

  override fun onBackPressed(): Boolean {
    LogUtil.debug(tag, "onBackPressed")
    if (container.visibility != View.GONE) {
      container.visibility = View.GONE
    }
    return true
  }
  
  private fun LayoutParams.setVerticalMargin(verticalMargin: Int = defaultMargin): LayoutParams{
    setMargins(0, verticalMargin, 0, verticalMargin)
    return this
  }
}