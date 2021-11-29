package com.sword.floatball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;

import com.example.utilclass.ScreenSize;

public class FloatMenuDialog {
  private AppCompatDialog dialog;
  private final Context context;


  public FloatMenuDialog(Context context, View view) {
    this.context = context;
    
    init(view);
  }

  /**
   * 宽高，位置：LayoutParams
   * 设置默认可取消
   * 动画
   */
  @SuppressLint("RtlHardcoded")
  private void init(View view) {
    dialog = new AppCompatDialog(context);
    
    Point dimension = ScreenSize.getWindowSizeExcludeSystem(context);
    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    params.width = dimension.x / 2;
    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    view.setLayoutParams(params);
    dialog.setContentView(view);
    
    dialog.setCancelable(true);
    dialog.setCanceledOnTouchOutside(true);
    
    //动画
    dialog.getWindow().setWindowAnimations(R.style.dialog_anim_default);
    
    
  }
  
  /*private void animation() {
    TransitionManager.beginDelayedTransition();
  }*/
}
