package com.example.swordlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.List;

public class TabLayout extends LinearLayout {
  private List<MenuItem> menuItems;
  
  
  public TabLayout(Context context) {
    this(context, null);
  }

  public TabLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }
  
  public void setMenuItems(List<MenuItem> menuItems) {
    this.menuItems = menuItems;
  }
}
