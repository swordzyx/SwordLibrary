package sword.view.viewpager

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import sword.dp
import sword.windowHeight
import sword.windowWidth

class ViewPagerFragment(val context: Context) {
  
  fun createDialog(width: Int = windowWidth, height: Int = windowHeight) {
    val dialog = sword.utils.SwordDialog(context)
    dialog.customView(createViewWithHorizontalTab()).setSize(width, height)
    dialog.show()
  }
  
  /**
   * 初始化 ViewPager 和侧边的 TabLayout，
   */
  private fun createViewWithHorizontalTab(tabWidth: Int = 40.dp): ViewGroup {
    val contentView = LinearLayout(context).apply {
      orientation = LinearLayout.HORIZONTAL
    }
    
    val tabLayout = TabLayout(context).apply {
      setBackgroundColor(Color.parseColor("#2B2B2B"))
      orientation = LinearLayout.VERTICAL
      currentClickIndex(0)
    }
    contentView.addView(tabLayout, LinearLayout.LayoutParams(tabWidth, ViewGroup.LayoutParams.MATCH_PARENT))
    
    val viewPager = ViewPager(context).apply {
      setBackgroundColor(Color.WHITE)
      currentItem = 0
      adapter = ViewPagerAdapter(arrayOf(""))
    }
    val lp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT).apply {
      weight = 1f
    }
    contentView.addView(viewPager, lp)
    
    tabLayout.bind(viewPager)
    return contentView
  }
  
  
}