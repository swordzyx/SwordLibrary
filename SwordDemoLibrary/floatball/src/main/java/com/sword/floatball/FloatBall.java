package com.sword.floatball;

import android.content.Context;

public class FloatBall {
  private FloatMenuView floatMenuView;
  private FloatBallView floatBallView;
  
  public FloatBall(Context context) {
    floatBallView = new FloatBallView(context);
    floatMenuView = new FloatMenuView(context);
  }
  
  public void showFloatBall() {
    
  }
}
