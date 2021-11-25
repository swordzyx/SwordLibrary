package com.sword.floatball;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.text.Layout;
import android.transition.Scene;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.utilclass.LogUtil;
import com.example.utilclass.ScreenSize;

public class FloatBall {
  private static final int MARGIN_WITH_BALL = ScreenSize.dpToPx(5);

  private final FloatBallContainer floatBallContainer;
  private final WindowManager windowManager;
  //private final ViewGroup decorView;
  private WindowManager.LayoutParams floatLayout;

  private final int screenWidth;
  /*private int floatBallX = 0;
  private int floatBallY = 0;*/

  private static FloatBall instance;

  public static FloatBall getInstance(Activity activity) {
    if (instance == null) {
      instance = new FloatBall(activity);
    }
    return instance;
  }

  private FloatBall(Activity context) {
    screenWidth = ScreenSize.getWindowSizeExcludeSystem(context).x;

    floatBallContainer = new FloatBallContainer(context);

    //windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    windowManager = context.getWindowManager();
    floatLayout = layoutParams();
  }

  public void showFloatBall() {
    if (!floatBallContainer.isAttachedToWindow()) {
      windowManager.addView(floatBallContainer, floatLayout);
      floatBallContainer.setVisibility(View.VISIBLE);
    }
    floatBallContainer.showFloatBall();
  }

  public void removeFloatBall() {
    if (floatBallContainer.isAttachedToWindow()) windowManager.removeView(floatBallContainer);
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
