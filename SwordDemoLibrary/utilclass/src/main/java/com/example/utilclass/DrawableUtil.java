package com.example.utilclass;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.StateSet;

public class DrawableUtil {
  public static Drawable getRoundRectSelectorDrawable(int corners, int color) {
    if (Build.VERSION.SDK_INT >= 21) {
      //设置蒙版层
      Drawable maskDrawable = createRoundRectDrawable(corners, 0xffffffff);
      ColorStateList colorStateList = new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{(color & 0x00ffffff) | 0x19000000});
      return new RippleDrawable(colorStateList, new ColorDrawable(0x00000000), maskDrawable);
    } else {
      StateListDrawable stateListDrawable = new StateListDrawable();
      stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, createRoundRectDrawable(corners, (color & 0x00fffffff) | 0x19000000 ));
      stateListDrawable.addState(new int[]{android.R.attr.state_selected}, createRoundRectDrawable(corners, (color & 0x00ffffff) | 0x19000000));
      stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0x00000000));
      return stateListDrawable;
    }
  }

  public static Drawable createRoundRectDrawable(int corners, int color) {
    ShapeDrawable shape = new ShapeDrawable(new RoundRectShape(new float[]{corners, corners, corners, corners, corners, corners, corners, corners}, null, null));
    shape.getPaint().setColor(color);
    return shape;
  }
}
