package sword.animate;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.widget.ImageView;

import sword.ScreenSize;
import sword.view.CircleView;


public class AnimatorUtil {
  
  public void startAnimate(ImageView target) {
    ObjectAnimator animator = ObjectAnimator.ofFloat(target, "alpha", ScreenSize.dp(200));
    animator.start();
  }
  
  public void propertyValuesHolder(CircleView target) {
    PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("radius", ScreenSize.dp(200));
    PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("offset", ScreenSize.dp(100));
    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, holder1, holder2);
    animator.start();
  }
  
  public void propertyValuesHolderWithKeyframe(CircleView target) {
    Keyframe keyframe1 = Keyframe.ofFloat(0, ScreenSize.dp(100));
    Keyframe keyframe2 = Keyframe.ofFloat(0.5f, ScreenSize.dp(250));
    Keyframe keyframe3 = Keyframe.ofFloat(1, ScreenSize.dp(200));
    
    PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("radius", keyframe1, keyframe2, keyframe3);
    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, holder);
    animator.start();
  }
  
  public void animatorSet(CircleView target) {
    ObjectAnimator animator1 = ObjectAnimator.ofFloat(target, "radius", ScreenSize.dp(200));
    ObjectAnimator animator2 = ObjectAnimator.ofFloat(target, "offset", ScreenSize.dp(100));
    
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(animator1, animator2);
    animatorSet.start();
  }
}
