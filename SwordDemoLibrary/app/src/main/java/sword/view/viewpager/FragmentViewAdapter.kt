package sword.view.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentViewAdapter(fragmentManager: FragmentManager):
  FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
  private val fragmentList = mutableListOf<Fragment>()

  /**
   * 返回页面的数量
   */
  override fun getCount(): Int {
    TODO("Not yet implemented")
  }

  /**
   * 根据 [position] 拿到对应的 Fragment
   */
  override fun getItem(position: Int): Fragment {
    TODO("Not yet implemented")
  }

}