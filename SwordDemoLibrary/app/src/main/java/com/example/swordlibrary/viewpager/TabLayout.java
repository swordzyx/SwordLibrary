package com.example.swordlibrary.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.swordlibrary.R;
import com.sword.LogUtil;
import com.sword.ScreenSize;

import java.util.ArrayList;
import java.util.List;

public class TabLayout extends LinearLayout {
  public static final String TAG = "TabLayout";
  public static final int marginEdge = ScreenSize.dp(8);
  
  private final List<MenuItemView> menuItemViews;
  
  final int[] iconSelectedResArray;
  final int[] iconResArray;
  
  public TabLayout(Context context) {
    this(context, null);
  }

  public TabLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    iconResArray = new int[]{R.drawable.user, R.drawable.feedback, R.drawable.float_menu_auth, R.drawable.switch_account};
    iconSelectedResArray = new int[]{R.drawable.user_selected, R.drawable.feedback_selected, R.drawable.float_menu_auth_selected, R.drawable.switch_account_selected};
    
    menuItemViews = new ArrayList<>();
  }

  public void bind(ViewPager viewPager) {
    LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
    params.weight = 1;
    for (int i = 0; i<iconResArray.length; i++) {
      MenuItemView itemView = new MenuItemView(getContext());
      itemView.setItemViewData(iconResArray[i], FloatBallData.floatBallData.titleStringArray[i]);
      itemView.setTag(i);
      itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          viewPager.setCurrentItem((int)itemView.getTag());
          LogUtil.debug(TAG, "click tab " + itemView.getTag());
        }
      });
      
      menuItemViews.add(itemView);
      addView(itemView);
    }
    
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LogUtil.debug("onPageScrolled, position: " + position + ", positinoOffset: " + positionOffset + ", positionOffsetPixels: " + positionOffsetPixels);
      }

      @Override
      public void onPageSelected(int position) {
        LogUtil.debug(TAG, "onPageSelected, position: " + position);
        onSelected(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {
        LogUtil.debug(TAG, "onPageScrollStateChanged, state: " + state);
      }
    });
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int h = (b - t - (marginEdge << 1)) / menuItemViews.size();
    LinearLayout.LayoutParams lp;
    for (MenuItemView menuItemView : menuItemViews) {
      lp = (LinearLayout.LayoutParams) menuItemView.getLayoutParams();
      lp.topMargin = lp.bottomMargin = (h - menuItemView.getMeasuredHeight()) >> 1;
    }
    super.onLayout(changed, l, t, r, b);
  }

  private void onSelected(int position) {
    if (menuItemViews == null || menuItemViews.size() <= 0) {
      return;
    }
    
    for (int i=0; i<menuItemViews.size(); i++) {
      int iconResId;
      if (position == i) {
        iconResId = iconSelectedResArray[i];
      } else {
        iconResId = iconResArray[i];
      }
      menuItemViews.get(i).setIconView(iconResId);
    }
  }
}
