package sword.pages

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.example.swordlibrary.R
import sword.dp
import sword.view.viewpager.TabLayout
import sword.view.viewpager.ViewPagerAdapter

class HomePage(val context: Context): Page {
  override val rootView by lazy { 
    createHomeView()
  }
  
  private val navigateContent = mapOf(
    "首页" to R.drawable.home_page,
    "发现" to R.drawable.discover,
    "消息" to R.drawable.message,
    "我的" to R.drawable.mime
  )
  
  private fun createHomeView(): View {
    val viewPager = ViewPager(context).apply {
      adapter = ViewPagerAdapter()
    }
    val tabLayout = TabLayout(context).apply {
      navigateContent.forEach { item ->
        addItem(TabLayout.TabItemView(context, item.key, item.value))
      }
      bind(viewPager)
    }

    return LinearLayout(context).apply {
      orientation = LinearLayout.VERTICAL
      addView(
        viewPager, 
        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0).apply { 
          weight = 1f 
        })
      addView(tabLayout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50.dp))
    }
  }

  
}