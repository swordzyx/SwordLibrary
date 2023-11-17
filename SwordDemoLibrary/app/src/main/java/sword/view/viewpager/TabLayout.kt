package sword.view.viewpager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.PagerTitleStrip
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import sword.dp
import sword.logger.SwordLog
import sword.view.floatball.FloatBallData

class TabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
  LinearLayout(context, attrs) {
  private val tag = "TabLayout"
  private val marginEdge = 8.dp
  private val tabItemViews = mutableListOf<TabItemView>()
  private var selectedIndex = 0
  
  init {
    orientation = HORIZONTAL
    setBackgroundColor(Color.WHITE)
  }

  fun currentClickIndex(index: Int) {
    tabItemViews[index].performClick()
  }
  
  fun addItem(itemView: TabItemView) {
    tabItemViews.add(itemView)
    //记录这个 item 的位置
    itemView.tag = tabItemViews.size - 1
    addView(
      itemView, 
      LayoutParams(0, LayoutParams.MATCH_PARENT).apply {
      weight = 1f
    })
  }

  /**
   * 建立导航栏与 ViewPager 之间的关联
   */
  fun bind(viewPager: ViewPager) {
    viewPager.addOnPageChangeListener(object : OnPageChangeListener {
      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        SwordLog.debug("onPageScrolled, position: $position, positinoOffset: $positionOffset, positionOffsetPixels: $positionOffsetPixels")
      }

      override fun onPageSelected(position: Int) {
        SwordLog.debug(tag, "onPageSelected, position: $position")
        if (tabItemViews.size <= 0) {
          return
        }
        
        tabItemViews[selectedIndex].unSelected()
        tabItemViews[position].selected()
        selectedIndex = position
      }

      override fun onPageScrollStateChanged(state: Int) {
        SwordLog.debug(tag, "onPageScrollStateChanged, state: $state")
      }
    })
  }

  @SuppressLint("ViewConstructor")
  class TabItemView(context: Context) : LinearLayout(context, null) {
    private val tag = "TabItemView"
    private val iconView = ImageView(context).apply { 
      scaleType = ImageView.ScaleType.FIT_CENTER
    }
    private val titleView = TextView(context).apply { 
      textSize = 5f.dp
      setTextColor(0xA4A4A4)
    }
    
    constructor(context: Context, title: String, iconRes: Int): this(context){
      iconView.setImageResource(iconRes)
      titleView.text = title
    }
    
    constructor(context: Context, title: String, drawable: Drawable): this(context) {
      iconView.setImageDrawable(drawable)
      titleView.text = title
    }
    
    init {
      orientation = VERTICAL
      setBackgroundColor(Color.BLUE)
      val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
      addView(iconView, lp)
      addView(titleView, lp)
      SwordLog.debug(tag, "addView")
    }
    
    fun setViewResource(iconRes: Int, title: String) {
      iconView.setImageResource(iconRes)
      titleView.text = title
    }
    
    fun selected() {
      iconView.isSelected = true
      titleView.isSelected = true
    }
    
    fun unSelected() {
      iconView.isSelected = false
      titleView.isSelected = false
    }
  }
}