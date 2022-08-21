package com.example.swordlibrary;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.widget.ImageView;

import com.example.utilclass.ScreenSizeUtil;

public class AnimatorUtil {
  private final ScreenSizeUtil sizeUtil = new ScreenSizeUtil();
  
  public void startAnimate(ImageView target) {
    ObjectAnimator animator = ObjectAnimator.ofFloat(target, "alpha", sizeUtil.dpToPx(200));
    animator.start();
  }
  
  public void propertyValuesHolder(CircleView target) {
    PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("radius", sizeUtil.dpToPx(200));
    PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("offset", sizeUtil.dpToPx(100));
    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, holder1, holder2);
    animator.start();
  }
  
  public void propertyValuesHolderWithKeyframe(CircleView target) {
    Keyframe keyframe1 = Keyframe.ofFloat(0, sizeUtil.dpToPx(100));
    Keyframe keyframe2 = Keyframe.ofFloat(0.5f, sizeUtil.dpToPx(250));
    Keyframe keyframe3 = Keyframe.ofFloat(1, sizeUtil.dpToPx(200));
    
    PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("radius", keyframe1, keyframe2, keyframe3);
    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, holder);
    animator.start();
  }
  
  public void animatorSet(CircleView target) {
    ObjectAnimator animator1 = ObjectAnimator.ofFloat(target, "radius", sizeUtil.dpToPx(200));
    ObjectAnimator animator2 = ObjectAnimator.ofFloat(target, "offset", sizeUtil.dpToPx(100));
    
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(animator1, animator2);
    animatorSet.start();
  }
}
