package com.sword.floatball;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * {@hide}
 */
public class FloatBallView2 extends AppCompatImageView {
	public FloatBallView2(@NonNull Context context) {
		super(context);
		
		setImageResource(R.mipmap.floatball);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}
