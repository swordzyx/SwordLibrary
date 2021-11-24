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
import androidx.appcompat.widget.AppCompatImageView;

import androidx.annotation.NonNull;

import com.example.utilclass.LogUtil;
import com.example.utilclass.ScreenSize;

/**
 * {@hide}
 */
public class FloatBallView2 extends AppCompatImageView {
	private static final int WAKE_UP_TIME = 3000;

	private final GestureDetector gestureDetector;

	private final Runnable sleepRunnable = new SleepRunnable();

	private float offsetX = 0;
	private float offsetY = 0;
	private boolean sleep = false;

	public FloatBallView2(@NonNull Context context) {
		super(context);

		FloatBallGestureListener listener = new FloatBallGestureListener();
		gestureDetector = new GestureDetector(context, listener);
		
		setBackgroundColor(Color.WHITE);
		setImageResource(R.mipmap.floatball);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//canvas.translate(offsetX, offsetY);
		super.onDraw(canvas);
	}

	/*public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
		invalidate();
	}*/

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector != null) {
			gestureDetector.onTouchEvent(event);
		}
		return true;
	}

	private boolean scrolled = false;
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
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			LogUtil.debug("onSingleUp, scrolled: " + scrolled);
			return false;
		}

		//滚动
		@SuppressLint("ObjectAnimatorBinding")
		@Override
		public boolean onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
			LogUtil.debug("onScroll");
			if (!scrolled) {
				removeCallbacks(sleepRunnable);
				scrolled = true;
			}

			/*offsetX += distanceX;
			offsetY += distanceY;
			invalidate();*/
			scrollBy((int) distanceX, (int) distanceY);
			
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			LogUtil.debug("onFling, scrolled" + scrolled);
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
			//offsetX -= getWidth() / 2f;
			scrollTo(getLeft() - getWidth()/2, getTop());
		} else {
			scrollTo(getLeft() + getWidth()/2, getTop());
			//offsetX += getWidth() / 2f;
		}
		invalidate();
	}

	private void wakeUp() {
		if (!sleep) return;
		
		sleep = false;

		if (getLeft() == 0) {
			scrollTo(getLeft() + getWidth()/2, getTop());
		} else {
			scrollTo(getLeft() - getWidth()/2, getTop());
		}
		invalidate();
	}

	private final AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {
		@Override
		public void onAnimationEnd(Animator animation) {
			scrolled = false;
			postSleepRunnable();
		}
	};


}
