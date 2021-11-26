package com.sword.floatball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.example.utilclass.LogUtil;
import com.example.utilclass.ScreenSize;

public class FloatBallContainer extends ViewGroup implements /*View.OnClickListener, */FloatMenuView.OnMenuItemClickListener {
  private static final int WAKE_UP_TIME = 10000 * 60;
  private static final int MARGIN_WITH_BALL = ScreenSize.dpToPx(5);
  private static final int MARGIN_WITH_EDGE = ScreenSize.dpToPx(3);

  private final FloatBallView2 floatBallView;
  private final FloatMenuView floatMenuView;

  private final ViewDragHelper dragHelper;
  //private final GestureDetector gestureDetector;
  private int floatBallOffsetX;
  private int floatBallOffsetY;
  boolean floatBallOnLeft = true;
  boolean sleep = false;

  public FloatBallContainer(Context context) {
    super(context);

    floatBallView = new FloatBallView2(context);
    //floatBallView.setOnClickListener(this);
    addView(floatBallView);

    floatMenuView = new FloatMenuView(context);
    floatMenuView.setMenuItemListener(this);
    floatMenuView.setVisibility(View.INVISIBLE);
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
      menuLeft = floatBallView.getRight() + MARGIN_WITH_BALL;
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

  boolean downOutofMenu = false;

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    /*if (dragHelper.shouldInterceptTouchEvent(event)) {
      return true;
    }
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        downOutofMenu = touchOutofMenu(event) && floatMenuVisiable();
        LogUtil.debug("onInterceptTouchEvent touchOutofMenu: " + downOutofMenu);
        return downOutofMenu;
      case MotionEvent.ACTION_UP:
        if (downOutofMenu && touchOutofMenu(event) && floatMenuVisiable()) {
          LogUtil.debug("onInterceptTouchEvent switchFloatMenuVisibility");
          switchFloatMenuVisibility();
          return true;
        }
      default:
        break;
    }
    return false;*/
    /*View view  = dragHelper.findTopChildUnder((int) event.getX(), (int) event.getY());
    LogUtil.debug("find view , x : " + event.getX() + "-- y: " + event.getY() + "-- floatBallX: " + floatBallView.getX() + "-- floatBallY: " + floatBallView.getY() + " find status: " + (view != null));
    LogUtil.debug("shouldInterceptTouchEvent: " + dragHelper.shouldInterceptTouchEvent(event));
    return dragHelper.shouldInterceptTouchEvent(event);*/
    return true;
  }
  
  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        downOutofMenu = touchOutofMenu(event);
        
        LogUtil.debug("onTouchEvent touchOutofMenu: " + downOutofMenu);
      case MotionEvent.ACTION_UP:
        if (downOutofMenu && touchOutofMenu(event) && floatMenuVisiable()) {
          LogUtil.debug("onTouchEvent switchFloatMenuVisibility");
          switchFloatMenuVisibility();
        }
      default:
        break;
    }
    /*if (event.getAction() == MotionEvent.ACTION_DOWN) {
      View view  = dragHelper.findTopChildUnder((int) event.getX(), (int) event.getY());
      LogUtil.debug("find view , x : " + event.getX() + "-- y: " + event.getY() + "-- floatBallX: " + floatBallView.getX() + "-- floatBallY: " + floatBallView.getY() + " find status: " + (view != null));
    }*/
    dragHelper.processTouchEvent(event);
    return true;
  }

  private boolean touchOutofMenu(MotionEvent event) {
    LogUtil.debug("touchX: " + event.getX() + "; touchY: " + event.getY() + "; floatMenu bottom: " + floatMenuView.getBottom() + "; floatMenu top: " + floatMenuView.getTop() + "; floatMenu right: " + floatMenuView.getRight());
    return event.getY() < floatMenuView.getTop() || event.getY() > floatMenuView.getBottom() || event.getX() > floatMenuView.getRight();
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

  private void hideFloatMenu() {
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

  private void floatBallClick() {
    LogUtil.debug("FloatBall Click");
    if (sleep) {
      floatBallWakeUp();
      postSleepRunnable();
      return;
    }
    removeCallbacks(sleepRunnable);
    switchFloatMenuVisibility();
  }

  private void switchFloatMenuVisibility() {
    if (floatMenuVisiable()) {
      hideFloatMenu();
    } else {
      showFloatMenu();
    }
  }
  
  private boolean floatMenuVisiable() {
    return floatMenuView.getVisibility() == View.VISIBLE;
  }

  @Override
  public void onMenuItemClick(int index) {
    LogUtil.debug("float item clicked, index: " + index);
  }

  class DragCallback extends ViewDragHelper.Callback {

    @Override
    public boolean tryCaptureView(@NonNull View child, int pointerId) {
      LogUtil.debug("tryCaptureView");
      /*LogUtil.debug("chile == floatBallView: " + (child == floatBallView) + "; sleep: " + sleep);
      return child == floatBallView && !sleep;*/
      return true;
    }

    @Override
    public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
      LogUtil.debug("clampViewPositionHorizontal left: " + left + "-- dx: " + dx);
      return left;
    }

    @Override
    public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
      LogUtil.debug("clampViewPositionVertical top: " + top + "--dy: " + dy);
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
      LogUtil.debug("onViewRelease, xvel: " + xvel + "--yvel: " + yvel);
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
  
  class GestureCallback extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
      if ((floatMenuVisiable() && touchOutofMenu(e)) || dragHelper.findTopChildUnder((int) e.getX(), (int) e.getY()) == floatBallView) {
        switchFloatMenuVisibility();
      }
      return true;
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
