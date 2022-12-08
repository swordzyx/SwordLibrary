package com.example.swordlibrary.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sword.ScreenSize;

/**
 * 悬浮菜单封装类
 */
class MenuItemView extends LinearLayout {
    private final ImageView iconView;
    private final TextView titleView;
    int verticalPadding = ScreenSize.dp(3);
    
    public MenuItemView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        iconView = new ImageView(context);
        iconView.setPadding(ScreenSize.dp(3), ScreenSize.dp(3), ScreenSize.dp(3), ScreenSize.dp(1));
        addView(iconView, lp);

        titleView =  new TextView(context);
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
        titleView.setGravity(Gravity.CENTER);
        lp.topMargin = verticalPadding;
        addView(titleView, lp);
    }

    void setItemViewData(int resId, String title) {
        iconView.setImageResource(resId);
        titleView.setText(title);
    }
    
    void setIconView(int resId) {
        iconView.setImageResource(resId);
    }
    
    void unRead(int num) {
        
    }
}