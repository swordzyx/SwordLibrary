package sword.view.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import sword.dp
import sword.logger.SwordLog
import sword.view.floatball.FloatBallData
import sword.view.floatball.MenuItemView

class TabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
  LinearLayout(context, attrs) {
  private val tag = "TabLayout"
  private val marginEdge = dp(8)
  private val menuItemViews: MutableList<MenuItemView>?
  lateinit var iconSelectedResArray: IntArray
  lateinit var iconResArray: IntArray

  init {
    menuItemViews = ArrayList()
  }

  fun currentClickIndex(index: Int) {
    menuItemViews!![index].performClick()
  }

  fun bind(viewPager: ViewPager) {
    val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0).apply {
      weight = 1f
    }
    iconResArray.forEach { i ->
      val itemView = MenuItemView(context)
      itemView.setItemViewData(iconResArray[i], FloatBallData.floatBallData.titleStringArray[i])
      itemView.tag = i
      itemView.setOnClickListener {
        viewPager.currentItem = itemView.tag as Int
        SwordLog.debug(tag, "click tab " + itemView.tag)
      }
      menuItemViews!!.add(itemView)
      addView(itemView)
    }
    
    viewPager.addOnPageChangeListener(object : OnPageChangeListener {
      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        SwordLog.debug("onPageScrolled, position: $position, positinoOffset: $positionOffset, positionOffsetPixels: $positionOffsetPixels")
      }

      override fun onPageSelected(position: Int) {
        SwordLog.debug(tag, "onPageSelected, position: $position")
        onSelected(position)
      }

      override fun onPageScrollStateChanged(state: Int) {
        SwordLog.debug(tag, "onPageScrollStateChanged, state: $state")
      }
    })
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val h = (b - t - (marginEdge shl 1)) / menuItemViews!!.size
    var lp: LayoutParams
    for (menuItemView in menuItemViews) {
      lp = menuItemView.layoutParams as LayoutParams
      lp.bottomMargin = h - menuItemView.measuredHeight shr 1
      lp.topMargin = lp.bottomMargin
    }
    super.onLayout(changed, l, t, r, b)
  }

  private fun onSelected(position: Int) {
    if (menuItemViews == null || menuItemViews.size <= 0) {
      return
    }
    for (i in menuItemViews.indices) {
      var iconResId: Int
      iconResId = if (position == i) {
        iconSelectedResArray[i]
      } else {
        iconResArray[i]
      }
      menuItemViews[i].setIconView(iconResId)
    }
  }
  
  class TabItemView {
    private val iconView: ImageView? = null
    private val titleView: TextView? = null
  } 
}