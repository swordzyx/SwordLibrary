package com.sword.floatball;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class FloatBall {
  public static final int FLOAT_ITEM_USER = 0;
  public static final int FLOAT_ITEM_FEEDBACK = 1;
  public static final int FLOAT_ITEM_SWITCH_ACCOUNT = 2;
  public static final int FLOAT_ITEM_GIFT = 3;

  private final FloatBallContainer floatBallContainer;
  private final WindowManager windowManager;
  private final WindowManager.LayoutParams floatLayout;
  private static FloatBall instance;

  public static FloatBall getInstance(Activity activity) {
    if (instance == null) {
      instance = new FloatBall(activity);
    }
    return instance;
  }

  private FloatBall(Activity context) {

    floatBallContainer = new FloatBallContainer(context);

    windowManager = context.getWindowManager();
    floatLayout = layoutParams();
  }

  public void setFloatMenuItemListener(OnMenuItemClickListener listener) {
    floatBallContainer.setFloatMenuItemListener(listener);
  }

  public void showFloatBall() {
    if (!floatBallContainer.isAttachedToWindow()) {
      windowManager.addView(floatBallContainer, floatLayout);
      floatBallContainer.setVisibility(View.VISIBLE);
    }
    floatBallContainer.showFloatBall();
  }

  @SuppressLint("RtlHardcoded")
  private WindowManager.LayoutParams layoutParams() {
    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
    params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
    params.gravity = Gravity.LEFT | Gravity.TOP;
    params.width = WindowManager.LayoutParams.MATCH_PARENT;
    params.height = WindowManager.LayoutParams.MATCH_PARENT;
    params.format = PixelFormat.TRANSPARENT;
    return params;
  }
}
