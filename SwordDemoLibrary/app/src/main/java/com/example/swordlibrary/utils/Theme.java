package com.example.swordlibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.StateSet;

import com.sword.ScreenSize;


public class Theme {
    public static final int DEFAULT_BUTTON_CORNER = (int) ScreenSize.dp(3);

    public static final int LOGO_WIDTH = (int) ScreenSize.dp(200);
    public static final int LOGO_HEIGHT = (int) ScreenSize.dp(40);

    public static Drawable createRoundRectDrawable(int color) {
        return createRoundRectDrawable(DEFAULT_BUTTON_CORNER, color);
    }

    public static Drawable createRoundRectDrawable(int corners, int color) {
        ShapeDrawable shape = new ShapeDrawable(new RoundRectShape(new float[]{corners, corners, corners, corners, corners, corners, corners, corners}, null, null));
        shape.getPaint().setColor(color);
        return shape;
    }

    public static Drawable createButtonStateDrawable(int enableColor, int disableColor) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, createRoundRectDrawable((int)ScreenSize.dp(4), enableColor));
        stateListDrawable.addState(StateSet.WILD_CARD, createButtonStateDrawable((int)ScreenSize.dp(4), disableColor));
        return stateListDrawable;
    }

    public static Drawable createCheckBoxDrawable(Drawable checkDrawable, Drawable unCheckDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checkDrawable);
        stateListDrawable.addState(StateSet.WILD_CARD, unCheckDrawable);
        return stateListDrawable;
    }

    public static Drawable getDrawableByResId(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    public static int getColor(Context context, int colorId) {
        return context.getResources().getColor(colorId);
    }

    public static float getDimen(Context context, int dimensId) {
        return context.getResources().getDimension(dimensId);
    }

    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

}
