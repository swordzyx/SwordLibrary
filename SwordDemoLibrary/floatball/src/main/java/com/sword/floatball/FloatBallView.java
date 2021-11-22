package com.sword.floatball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.appcompat.widget.AppCompatImageView;

import androidx.annotation.NonNull;

import com.example.utilclass.ScreenSizeUtil;

public class FloatBallView extends AppCompatImageView {
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final ScreenSizeUtil sizeUtil = new ScreenSizeUtil();
	private final int border = sizeUtil.dpToPx(2);
	private int borderAlpha = 15;
	private int innerCircleAlpha = 5;

	public FloatBallView(@NonNull Context context) {
		super(context);

		paint.setColor(Color.BLACK);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setStyle(Paint.Style.FILL);
		paint.setAlpha(innerCircleAlpha);
		canvas.drawCircle((getLeft() + getRight()) / 2f, (getTop() + getBottom()) / 2f, getWidth() / 2f, paint);
		paint.setAlpha(borderAlpha);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(border);
		canvas.drawCircle((getLeft() + getRight()) / 2f, (getTop() + getBottom()) / 2f, getWidth() / 2f, paint);
		super.onDraw(canvas);
	}

	public void setInnerCircleAlpha(int alpha) {
		innerCircleAlpha = alpha;
	}

	public void setBorderAlpha(int alpha) {
		borderAlpha = alpha;
	}
}
