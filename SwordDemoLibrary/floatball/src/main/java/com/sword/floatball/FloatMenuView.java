package com.sword.floatball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utilclass.LogUtil;
import com.example.utilclass.ScreenSize;

import java.util.ArrayList;
import java.util.List;

/** {@hide} */
public class FloatMenuView extends ViewGroup {
  private static final int BACKGROUND_ALPHA = (int) (0.3 * 0xff);

  private OnMenuItemClickListener listener;
  private final GestureDetector gestureDetector;

  final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final List<MenuItem> items = new ArrayList<>();

  private final int innerPadding = ScreenSize.dpToPx(3);
  private final int itemMargin = ScreenSize.dpToPx(8);


  public FloatMenuView(Context context) {
    this(context, null);
  }

  public FloatMenuView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initMenuItems(context);

    backgroundPaint.setColor(Color.BLACK);
    backgroundPaint.setAlpha(BACKGROUND_ALPHA);

    gestureDetector = new GestureDetector(context, new CustomGestureDetector());
  }

  private void initMenuItems(Context context) {
    LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    ImageView accountIcon = buildImageView(context, params, R.drawable.user);
    TextView accountTitle = buildTextView(context, params, "账户");
    items.add(new MenuItem(accountIcon, accountTitle));
    addView(accountIcon);
    addView(accountTitle);

    ImageView feedBackIcon = buildImageView(context, params, R.drawable.feedback);
    TextView feedBackTitle = buildTextView(context, params, "客服");
    items.add(new MenuItem(feedBackIcon, feedBackTitle));
    addView(feedBackIcon);
    addView(feedBackTitle);

    ImageView switchAccountIcon = buildImageView(context, params, R.drawable.switch_account);
    TextView switchAccountTitle = buildTextView(context, params, "切换");
    items.add(new MenuItem(switchAccountIcon, switchAccountTitle));
    addView(switchAccountIcon);
    addView(switchAccountTitle);

    ImageView giftIcon = buildImageView(context, params, R.drawable.gift);
    TextView giftTitle = buildTextView(context, params, "礼包");
    items.add(new MenuItem(giftIcon, giftTitle));
    addView(giftIcon);
    addView(giftTitle);
  }

  private TextView buildTextView(Context context, LayoutParams params, String text) {
    TextView view = new TextView(context);
    view.setLayoutParams(params);
    view.setText(text);
    view.setTextColor(Color.WHITE);
    view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 35);
    view.setGravity(Gravity.CENTER);
    return view;
  }

  private ImageView buildImageView(Context context, LayoutParams params, int imageRes) {
    ImageView view = new ImageView(context);
    //view.setScaleType(ImageView.ScaleType.CENTER_CROP);
    view.setLayoutParams(params);
    view.setImageResource(imageRes);
    /*view.setBackgroundColor(Color.BLACK);
    view.setAlpha(BACKGROUND_ALPHA);*/
    return view;
  }

  /**
   * 遍历每个子 View，将子 View 的位置和尺寸传给它们的 layout 方法，用于将位置和尺寸传给它们
   */
  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    LogUtil.debug("FloatMenuView - onLayout, left: " + l + "; top: " + t + "; right: " + r + "; bottom: " + b);
    int left = itemMargin;
    for (MenuItem item : items) {
      LogUtil.debug("left: " + left + "; width: " + item.getWidth());
      item.layout(left, getPaddingTop());
      left = left + item.getWidth() + itemMargin;
    }
  }

  /**
   * 1. 遍历每个子 View，测量子 View。测量完成后，得出子 View 的实际尺寸
   * 2. 所有的子 View 都测量完毕之后，计算出自己的尺寸，通过 setMeasuredDimension(width, height) 保存
   */
  @SuppressLint("Range")
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int measureWidth = 0;
    int measureHeight = 0;
    for (MenuItem item : items) {
      item.measure();
      measureWidth += item.getWidth() + itemMargin;
      measureHeight = item.getHeight();
    }
    setMeasuredDimension(measureWidth + itemMargin, measureHeight);
    LogUtil.debug("FloatMenuView - onMeasure, measureWidth: " + getMeasuredWidth() + "; measureHeight: " + getMeasuredHeight());
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    canvas.drawRect(0f, 0f, (float) getWidth(), (float) getHeight(), backgroundPaint);
    //canvas.drawColor(BACKGROUND_ALPHA);
    super.dispatchDraw(canvas);
  }

  private float downX = 0f;

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    LogUtil.debug("FloatMenuView - onInterceptTouchEvent event.getAction = " + ev.getAction());
    return true;
  }

  //FloatMenuView 的父 View 是 FloatBallContainer，如果 FloatMenuView#onTouchEvent 返回 false，那么即使 FloatMenuView#onInterceptTouchEvent 返回 true，FloatBallContainer#onTouchEvent 依然会被执行。
  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return gestureDetector.onTouchEvent(event);
  }

  public void setMenuItemListener(OnMenuItemClickListener listener) {
    this.listener = listener;
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

      int titleTop = top + icon.getMeasuredHeight() + innerPadding;
      title.layout(left, titleTop, left + title.getMeasuredWidth(), titleTop + title.getMeasuredHeight());
    }

    int getWidth() {
      return Math.max(icon.getMeasuredWidth(), title.getMeasuredWidth());
    }

    int getHeight() {
      return title.getMeasuredHeight() + icon.getMeasuredHeight() + 2 * innerPadding;
    }

    @SuppressLint("Range")
    void measure() {
      int defaultSpec = MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST);
      if (icon == null || title == null) {
        return;
      }
      icon.measure(defaultSpec, defaultSpec);

      title.measure(defaultSpec, defaultSpec);
      LogUtil.debug("FloatMenuView - icon width: " + icon.getMeasuredWidth() + "; height: " + icon.getMeasuredHeight());
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
    
    /*@SuppressLint("Range")
    int measureHeight(LayoutParams layoutParams) {
      switch (layoutParams.height) {
        case LayoutParams.MATCH_PARENT:
          return MeasureSpec.makeMeasureSpec(FloatMenuView.this.getMeasuredHeight(), MeasureSpec.EXACTLY);
        case LayoutParams.WRAP_CONTENT:
          return ;
        default:
          return MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
      }
    }*/
  }

  class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onDown(MotionEvent e) {
      return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
      if (listener != null) {
        listener.onMenuItemClick(findClickItemIndex(e));
      }
      return true;
    }
  }

  /**
   * 根据点击的位置判断点击的是哪个 item 项。悬浮菜单中的所有菜单项都是等宽的，定义每个子项的范围是“子项的宽度 + 子项前面的 itemMargin”
   * 更好的设计是：每个子项的点击范围仅为其宽度，其宽度是“内容宽度 + 水平 padding”，这样的话，每个子项在测量时就需要加上设置的 padding
   */
  private int findClickItemIndex(MotionEvent event) {
    int itemWidth = (getWidth() - itemMargin) / 4;
    LogUtil.debug("FloatMenuView - itemWidth: " + itemWidth + "; clickX: " + event.getX() + "; menuLeft: " + getLeft());
    /*for (int i=0; i<items.size(); i++) {
      MenuItem item = items.get(i);
      
    }*/
    return (int) (event.getX() / itemWidth);
  }
}
