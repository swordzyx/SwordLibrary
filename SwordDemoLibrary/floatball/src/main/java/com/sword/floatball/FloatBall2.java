package com.sword.floatball;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.example.utilclass.ScreenSize;

public class FloatBall2 {
  private static final int MARGIN_WITH_BALL = ScreenSize.dpToPx(5);

  private final WindowManager windowManager;
  private WindowManager.LayoutParams floatLayout ;
  private final FloatBallView2 ballView;
  
  private final int screenWidth;
  private int floatBallX = 0;
  private int floatBallY = 0;

  private static FloatBall2 instance;

  public static FloatBall2 getInstance(Activity activity) {
    if (instance == null) {
      instance = new FloatBall2(activity);
    }
    return instance;
  }

  private FloatBall2(Activity context) {
    screenWidth = ScreenSize.getWindowSizeExcludeSystem(context).x;


    ballView = new FloatBallView2(context);
    
    windowManager = context.getWindowManager();
    floatLayout = layoutParams();
  }

  public void showFloatBall() {
    windowManager.addView(ballView, floatLayout);
  }

  public void removeFloatBall() {
    if (ballView.isAttachedToWindow()) windowManager.removeView(ballView);
  }
  
  @SuppressLint("RtlHardcoded")
  private WindowManager.LayoutParams layoutParams() {
    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
    params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
    params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    params.format = PixelFormat.TRANSPARENT;
    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    return params;
  }
}
