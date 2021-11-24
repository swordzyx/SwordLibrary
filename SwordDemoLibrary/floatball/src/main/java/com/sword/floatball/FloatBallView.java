package com.sword.floatball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.utilclass.ScreenSizeUtil;

/**
 * {@hide}
 */
public class FloatBallView extends AppCompatImageView {
	private static final int WAKE_UP_TIME = 3000;

	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private ScreenSizeUtil sizeUtil = null;
	private FloatBallGestureListener listener = null;
	private GestureDetector gestureDetector = null;
	private OverScroller scroller = null;

	private final Runnable sleepRunnable = new SleepRunnable();

	private final int border = sizeUtil.dpToPx(2);
	private int screenWidth;
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
		scroller = new OverScroller(context);
		sizeUtil = new ScreenSizeUtil();

		screenWidth = sizeUtil.getWindowSizeExcludeSystem(context).x;
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

	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
		invalidate();
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
			if (sleep) {
				wakeUp();
				postSleepRunnable();
			} else {
				performClick();
			}
			return false;
		}

		//滚动
		@SuppressLint("ObjectAnimatorBinding")
		@Override
		public boolean onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
			switch (currentEvent.getAction()) {
				case MotionEvent.ACTION_UP:
					ObjectAnimator moveEdge;
					if (currentEvent.getX() > screenWidth / 2f) {
						moveEdge = ObjectAnimator.ofFloat(this, "offsetX", offsetX, screenWidth - getLeft());
					} else {
						moveEdge = ObjectAnimator.ofFloat(this, "offsetX", offsetX, 0);
					}
					moveEdge.addListener(animatorListener);
					moveEdge.start();
					break;
				case MotionEvent.ACTION_MOVE:
					offsetX = distanceX;
					offsetY = distanceY;
					invalidate();
					break;
				default:
					break;
			}
			return false;
		}
	}

	private void postSleepRunnable() {
		postDelayed(sleepRunnable, WAKE_UP_TIME);
	}

	private void removeSleepRunnable() {
		removeCallbacks(sleepRunnable);
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
			offsetX -= getWidth() / 2f;
		} else {
			offsetX += getWidth() / 2f;
		}
		invalidate();
	}

	private void wakeUp() {
		if (!sleep) return;
		sleep = false;

		if (getLeft() == 0) {
			offsetX += getWidth() / 2f;
		} else {
			offsetX -= getWidth() / 2f;
		}
		invalidate();
	}

	private final AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {
		@Override
		public void onAnimationEnd(Animator animation) {
			postSleepRunnable();
		}
	};


}
