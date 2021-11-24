package com.sword.floatball;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.example.utilclass.LogUtil;
import com.example.utilclass.ScreenSize;

public class FloatBallContainer extends ViewGroup implements View.OnClickListener, FloatMenuView.OnMenuItemClickListener {
  private static final int MARGIN_WITH_BALL = ScreenSize.dpToPx(5);
  private final FloatBallView2 floatBallView;
  private final FloatMenuView floatMenuView;
  
  public FloatBallContainer(Context context) {
    super(context);
    
    floatBallView = new FloatBallView2(context);
    floatBallView.setOnClickListener(this);
    
    floatMenuView = new FloatMenuView(context);
    floatMenuView.setMenuItemListener(this);
    
    setBackgroundColor(Color.TRANSPARENT);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    floatBallView.measure(
        MeasureSpec.makeMeasureSpec(floatBallView.getMeasuredWidth(), MeasureSpec.AT_MOST), 
        MeasureSpec.makeMeasureSpec(floatBallView.getMeasuredHeight(), MeasureSpec.AT_MOST));
    
    floatMenuView.measure(
        MeasureSpec.makeMeasureSpec(floatMenuView.getMeasuredWidth(), MeasureSpec.AT_MOST),
        MeasureSpec.makeMeasureSpec(floatMenuView.getMeasuredHeight(), MeasureSpec.AT_MOST)
    );
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    floatBallView.layout(
        floatBallView.getLeft(),
        floatBallView.getTop(),
        floatBallView.getLeft() + floatBallView.getMeasuredWidth(),
        floatBallView.getTop() + floatBallView.getMeasuredHeight()
    );
    
    /*floatMenuView.layout(
        floatBallView.getRight() + MARGIN_WITH_BALL,
        floatBallView.getTop(),
        floatBallView.getRight() + MARGIN_WITH_BALL + floatMenuView.getMeasuredWidth(),
        floatBallView.getTop() + floatMenuView.getMeasuredHeight()
    );*/
  }
  
  private void showFloatMenu() {
    if (floatMenuView.getVisibility() != ViewGroup.VISIBLE) {
      floatMenuView.setVisibility(ViewGroup.VISIBLE);
    }
  }

  private void hideMenu() {
    if (floatMenuView.getVisibility() == ViewGroup.VISIBLE) {
      floatMenuView.setVisibility(ViewGroup.GONE);
    }
  }
  
  @Override
  public void onClick(View v) {
    if (floatMenuView.getVisibility() == View.VISIBLE) {
      hideMenu();
    } else {
      showFloatMenu();
    }
  }

  @Override
  public void onMenuItemClick(int index) {
    LogUtil.debug("float item clicked, index: " + index);
  }
}
