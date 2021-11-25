package com.sword.floatball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.example.utilclass.LogUtil;
import com.example.utilclass.ScreenSize;

public class FloatBallContainer extends ViewGroup implements View.OnClickListener, FloatMenuView.OnMenuItemClickListener {
  private static final int WAKE_UP_TIME = 3000;
  private static final int MARGIN_WITH_BALL = ScreenSize.dpToPx(5);
  private static final int MARGIN_WITH_EDGE = ScreenSize.dpToPx(3);

  private final FloatBallView2 floatBallView;
  private final FloatMenuView floatMenuView;

  private final ViewDragHelper dragHelper;
  private int floatBallOffsetX;
  private int floatBallOffsetY;
  boolean floatBallOnLeft = true;
  boolean sleep = false;

  public FloatBallContainer(Context context) {
    super(context);

    floatBallView = new FloatBallView2(context);
    floatBallView.setOnClickListener(this);
    addView(floatBallView);

    floatMenuView = new FloatMenuView(context);
    floatMenuView.setMenuItemListener(this);
    //floatMenuView.setVisibility(View.GONE);
    addView(floatMenuView);
    
    LogUtil.debug("add float ball and float menu");

    dragHelper = ViewDragHelper.create(this, new DragCallback());
  }

  @SuppressLint("Range")
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    LogUtil.debug("FloatballContainer onMeasure");
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    floatBallView.measure(
        MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST),
        MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST));

    LogUtil.debug("floatBallView width: " + floatBallView.getMeasuredWidth() + "  height: " + floatBallView.getMeasuredHeight());

    floatMenuView.measure(
        MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST),
        MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST)
    );
    LogUtil.debug("floatMenuView width: " + floatMenuView.getMeasuredWidth() + "  height: " + floatMenuView.getMeasuredHeight());
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    LogUtil.debug("FloatballContainer onLayout");
    int ballLeft = MARGIN_WITH_EDGE + floatBallOffsetX;
    int ballTop = getTop() + getMeasuredHeight() / 2 + floatBallOffsetY;
    floatBallView.layout(
        ballLeft,
        ballTop,
        ballLeft + floatBallView.getMeasuredWidth(),
        ballTop + floatBallView.getMeasuredHeight()
    );
    LogUtil.debug("floatBallView Left: " + floatBallView.getLeft() + "; top=" + floatBallView.getTop() + "; measureWidth=" + floatBallView.getMeasuredWidth() + "; measureHeight=" + floatBallView.getMeasuredWidth());

    int menuLeft;
    if (floatBallOnLeft) {
      menuLeft = ballLeft + MARGIN_WITH_BALL;
    } else {
      menuLeft = ballLeft - MARGIN_WITH_BALL - floatMenuView.getMeasuredWidth();
    }
    floatMenuView.layout(
        menuLeft,
        ballTop,
        menuLeft + floatMenuView.getMeasuredWidth(),
        ballTop + floatMenuView.getMeasuredHeight()
    );
    LogUtil.debug("floatMenuView Left: " + floatMenuView.getLeft() + "; top=" + floatMenuView.getTop() + "; measureWidth=" + floatMenuView.getMeasuredWidth() + "; measureHeight=" + floatMenuView.getMeasuredWidth());
  }

  @Override
  public boolean onInterceptHoverEvent(MotionEvent event) {
    return dragHelper.shouldInterceptTouchEvent(event);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    dragHelper.processTouchEvent(event);
    return true;
  }

  private void showFloatMenu() {
    if (floatMenuView.getVisibility() != View.VISIBLE) {
      floatMenuView.setVisibility(View.VISIBLE);
    }
  }

  public void showFloatBall() {
    LogUtil.debug("show float ball, visible: " + (floatBallView.getVisibility() == View.VISIBLE));
    if (floatBallView.getVisibility() != View.VISIBLE) {
      floatBallView.setVisibility(View.VISIBLE);
    }
  }

  private void hideMenu() {
    LogUtil.debug("show float ball, visible: " + (floatBallView.getVisibility() == View.VISIBLE));
    if (floatMenuView.getVisibility() == View.VISIBLE) {
      floatMenuView.setVisibility(View.INVISIBLE);
    }
    postSleepRunnable();
  }

  @Override
  protected void onAttachedToWindow() {
    LogUtil.debug("onAttachToWindow");
    super.onAttachedToWindow();
    postSleepRunnable();
  }

  @Override
  public void onClick(View v) {
    LogUtil.debug("FloatBall Click");
    if (sleep) {
      floatBallWakeUp();
      postSleepRunnable();
      return;
    }
    removeCallbacks(sleepRunnable);
    LogUtil.debug("show float menu, visible: " + (floatMenuView.getVisibility() == View.VISIBLE));
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

  class DragCallback extends ViewDragHelper.Callback {

    @Override
    public boolean tryCaptureView(@NonNull View child, int pointerId) {
      LogUtil.debug("chile == floatBallView: " + (child == floatBallView) + "; sleep: " + sleep);
      return child == floatBallView && !sleep; 
    }

    @Override
    public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
      return left;
    }

    @Override
    public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
      return top;
    }

    @Override
    public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
      LogUtil.debug("changeView == floatBallView: " + (changedView == floatBallView) + "; dx = " + dx + "; dy = " + dy);
      if (changedView == floatBallView) {
        floatBallOffsetX = dx;
        floatBallOffsetY = dy;
        requestLayout();
      }
    }

    @Override
    public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
      if (releasedChild != floatBallView) {
        return;
      }

      if (releasedChild.getLeft() >= getWidth() / 2) {
        dragHelper.settleCapturedViewAt(getWidth() - MARGIN_WITH_BALL, floatBallView.getTop());
        floatBallOnLeft = false;
      } else {
        dragHelper.settleCapturedViewAt(MARGIN_WITH_BALL, floatBallView.getTop());
        floatBallOnLeft = true;
      }
      postInvalidateOnAnimation();
    }
  }


  /**
   * computeScroll 在 View 重绘时会被自动调用。
   */
  @Override
  public void computeScroll() {
    if (dragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }

  private void postSleepRunnable() {
    postDelayed(sleepRunnable, WAKE_UP_TIME);
  }

  private final Runnable sleepRunnable = this::floatBallSleep;

  private void floatBallSleep() {
    sleep = true;
    floatBallOffsetY = 0;
    floatBallOffsetX = -(floatBallView.getWidth() / 2 + MARGIN_WITH_EDGE);
    requestLayout();
  }

  private void floatBallWakeUp() {
    sleep = false;
    floatBallOffsetY = 0;
    floatBallOffsetX = 0;
    requestLayout();
  }
}
