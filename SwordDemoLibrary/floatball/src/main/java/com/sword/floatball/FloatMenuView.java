package com.sword.floatball;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utilclass.ScreenSize;

import java.util.ArrayList;
import java.util.List;

/**{@hide}*/
public class FloatMenuView extends ViewGroup {
  private OnMenuItemClickListener listener;
  private final List<MenuItem> items = new ArrayList<>();
  private final int itemPadding = ScreenSize.dpToPx(4);
  
  
  int innerPadding = ScreenSize.dpToPx(2);
  private float menuItemWidth = Float.MAX_VALUE;
  

  public FloatMenuView(Context context) {
    super(context);
    
    initMenuItems(context);
  }
  
  private void initMenuItems(Context context) {
    LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    
    ImageView accountIcon = buildImageView(context, params, R.drawable.user);
    TextView accountTitle = buildTextView(context, params, "账户");
    items.add(new MenuItem(accountIcon, accountTitle));
    
    ImageView feedBackIcon = buildImageView(context, params, R.drawable.feedback);
    TextView feedBackTitle = buildTextView(context, params, "客服");
    items.add(new MenuItem(feedBackIcon, feedBackTitle));
    
    ImageView switchAccountIcon = buildImageView(context, params, R.drawable.switch_account);
    TextView switchAccountTitle = buildTextView(context, params, "切换");
    items.add(new MenuItem(switchAccountIcon, switchAccountTitle));
    
    ImageView giftIcon = buildImageView(context, params, R.drawable.gift);
    TextView giftTitle = buildTextView(context, params, "礼包");
    items.add(new MenuItem(giftIcon, giftTitle));
  }

  private TextView buildTextView(Context context, LayoutParams params, String text) {
    TextView view = new TextView(context);
    view.setLayoutParams(params);
    view.setText(text);
    return view;
  }
  
  private ImageView buildImageView(Context context, LayoutParams params, int imageRes) {
    ImageView view = new ImageView(context);
    view.setScaleType(ImageView.ScaleType.CENTER_CROP);
    view.setLayoutParams(params);
    view.setImageResource(imageRes);
    return view;
  }
  
  /**
   * 遍历每个子 View，将子 View 的位置和尺寸传给它们的 layout 方法，用于将位置和尺寸传给它们
   */
  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int left = itemPadding;
    for (MenuItem item : items) {
      item.layout(left, getPaddingTop());
      left = left + item.getWidth() + itemPadding;
    }
  }

  /**
   * 1. 遍历每个子 View，测量子 View。测量完成后，得出子 View 的实际尺寸
   * 2. 所有的子 View 都测量完毕之后，计算出自己的尺寸，通过 setMeasuredDimension(width, height) 保存
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    menuItemWidth = getMeasuredWidth() / 4f;
    for (MenuItem item : items) {
      item.measure(MeasureSpec.makeMeasureSpec((int) menuItemWidth, MeasureSpec.EXACTLY));
    }
    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
  }

  private float downX = 0f;
  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        downX = ev.getX();
        break;
      case MotionEvent.ACTION_UP:
        if (ev.getX() - downX > menuItemWidth) {
          break;
        }
        if (listener != null) {
          int index = (int)((ev.getX() - getLeft()) / menuItemWidth);
          listener.onMenuItemClick(index);
        }
        break;
    }
    return true;
  }

  /**
   * 悬浮菜单封装类
   */
  class MenuItem {
    ImageView icon;
    TextView title;

    MenuItem(ImageView icon, TextView title) {
      this.icon = icon;
      this.title = title;
    }

    void layout(int left, int top) {
      icon.layout(left, top, left + icon.getMeasuredWidth(), top + icon.getMeasuredWidth());
      
      int titleTop = top + icon.getMeasuredWidth() + innerPadding;
      title.layout(left, titleTop, left + title.getMeasuredWidth(), titleTop + title.getMeasuredHeight());
    }

    int getWidth() {
      return title.getMeasuredWidth();
    }

    void measure(int widthSpec) {
      if (icon == null || title == null) {
        return;
      }
      icon.measure(widthSpec, measureHeight(icon.getLayoutParams()));
      title.measure(widthSpec, measureHeight(title.getLayoutParams()));
    }

    /*int measureWidth(LayoutParams layoutParams) {
      switch (layoutParams.width) {
        case LayoutParams.MATCH_PARENT:
          return MeasureSpec.makeMeasureSpec(FloatMenuView.this.getMeasuredWidth(), MeasureSpec.EXACTLY);
        case LayoutParams.WRAP_CONTENT:
          return MeasureSpec.makeMeasureSpec(FloatMenuView.this.getMeasuredWidth(), MeasureSpec.AT_MOST);
        default:
          return MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
      }
    }*/
    
    int measureHeight(LayoutParams layoutParams) {
      switch (layoutParams.height) {
        case LayoutParams.MATCH_PARENT:
          return MeasureSpec.makeMeasureSpec(FloatMenuView.this.getMeasuredHeight(), MeasureSpec.EXACTLY);
        case LayoutParams.WRAP_CONTENT:
          return MeasureSpec.makeMeasureSpec(FloatMenuView.this.getMeasuredHeight(), MeasureSpec.AT_MOST);
        default:
          return MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
      }
    }
  }
  
  interface OnMenuItemClickListener {
    void onMenuItemClick(int index);
  }
}
