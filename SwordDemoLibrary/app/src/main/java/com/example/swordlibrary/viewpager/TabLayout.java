package com.example.swordlibrary.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class TabLayout extends LinearLayout {


  public TabLayout(Context context) {
    this(context, null);
  }

  public TabLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  private LinearLayout createMenuItemView(MenuItem item) {
    LinearLayout linearLayout = new LinearLayout(getContext());
    linearLayout.setOrientation(LinearLayout.VERTICAL);
    linearLayout.addView(item.icon);
    linearLayout.addView(item.title);
    return linearLayout;
  }

  public void bind(ViewPager viewPager, FloatBallData floatBallData) {
    List<MenuItem> menuItems = floatBallData.getFloatData(getContext());

    for (MenuItem item: menuItems) {
      addView(createMenuItemView(item));
    }

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {

      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }
}
