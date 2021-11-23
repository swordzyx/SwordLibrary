package com.sword.floatball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.utilclass.ScreenSizeUtil;

/**{@hide}*/
public class FloatBallView extends AppCompatImageView{
	private static final int WAKE_UP_TIME = 3000;
	
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private final ScreenSizeUtil sizeUtil = new ScreenSizeUtil();
	private FloatBallGestureListener listener = null;
	private GestureDetector gestureDetector = null;
	
	private Runnable sleepRunnable = new SleepRunnable();
	private 
	
	private final int border = sizeUtil.dpToPx(2);
	private int borderAlpha = 15;
	private int innerCircleAlpha = 5;
	private float offsetX = 0;
	private float offsetY = 0;
	private boolean sleep = false;

	public FloatBallView(@NonNull Context context) {
		super(context);

		paint.setColor(Color.BLACK);
		
		listener = new FloatBallGestureListener();
		gestureDetector = new GestureDetector(context, listener);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float centerX = (getLeft() + getRight()) / 2f + offsetX;
		float centerY = (getTop() + getBottom()) / 2f + offsetY;
		paint.setStyle(Paint.Style.FILL);
		paint.setAlpha(innerCircleAlpha);
		canvas.drawCircle(centerX, centerY, getWidth() / 2f, paint);
		paint.setAlpha(borderAlpha);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(border);
		canvas.drawCircle(centerX, centerY, getWidth() / 2f, paint);
		super.onDraw(canvas);
	}

	public void setInnerCircleAlpha(int alpha) {
		innerCircleAlpha = alpha;
	}

	public void setBorderAlpha(int alpha) {
		borderAlpha = alpha;
	}


	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector != null) {
			gestureDetector.onTouchEvent(event);
		}
		return true;
	}

	class FloatBallGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		//单击
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if(sleep) {
				wakeUp();
			} else {
				performClick();
			}
			return false;
		}

		//滚动
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			offsetX = distanceX;
			
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}
	
	private class SleepRunnable implements Runnable {
		@Override
		public void run() {
			sleep();
		}
	} 
	
	//休眠状态
	private void sleep() {
		if (sleep) return;
		sleep = true;
		
		if (getLeft() == 0) {
			offsetX -= getWidth()/2; 
		} else {
			offsetX += getWidth()/2;
		}
		invalidate();
	}
	
	private void wakeUp() {
		if (!sleep) return;
		sleep = false;

		if (getLeft() == 0) {
			offsetX += getWidth()/2;
		} else {
			offsetX -= getWidth()/2;
		}
		invalidate();
	}
}
