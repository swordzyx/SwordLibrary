package com.huxq17.floatball.libarary.floatball;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.buyi.huxq17.serviceagency.ServiceAgency;
import com.buyi.huxq17.serviceagency.exception.AgencyException;
import com.huxq17.floatball.libarary.FloatBallManager;
import com.huxq17.floatball.libarary.FloatBallUtil;
import com.huxq17.floatball.libarary.LocationService;
import com.huxq17.floatball.libarary.runner.ICarrier;
import com.huxq17.floatball.libarary.runner.OnceRunnable;
import com.huxq17.floatball.libarary.runner.ScrollRunner;
import com.huxq17.floatball.libarary.utils.MotionVelocityUtil;
import com.huxq17.floatball.libarary.utils.Util;


public class FloatBall extends FrameLayout implements ICarrier {

	private FloatBallManager floatBallManager;
	private ImageView imageView;
	private WindowManager.LayoutParams mLayoutParams;
	private WindowManager windowManager;
	private boolean isFirst = true;
	private boolean isAdded = false;
	private int mTouchSlop;
	/**
	 * flag a touch is click event
	 */
	private boolean isClick;
	private int mDownX, mDownY, mLastX, mLastY;
	private int mSize;
	private ScrollRunner mRunner;
	private int mVelocityX, mVelocityY;
	private MotionVelocityUtil mVelocity;
	private boolean sleep = false;
	private FloatBallCfg mConfig;
	private boolean mHideHalfLater = true;
	private boolean mLayoutChanged = false;
	private int mSleepX = -1;
	private boolean isLocationServiceEnable;
	private OnceRunnable mSleepRunnable = new OnceRunnable() {
		@Override
		public void onRun() {
			if (mHideHalfLater && !sleep && isAdded) {
				sleep = true;
				moveToEdge(false, sleep);
				mSleepX = mLayoutParams.x;
			}
		}
	};

	public FloatBall(Context context, FloatBallManager floatBallManager, FloatBallCfg config) {
		super(context);
		this.floatBallManager = floatBallManager;
		mConfig = config;
		try {
			ServiceAgency.getService(LocationService.class);
			isLocationServiceEnable = true;
		} catch (AgencyException e) {
			isLocationServiceEnable = false;
		}
		init(context);
	}

	/**
	 * 设置悬浮球图标，大小
	 * 初始化布局参数：layoutParams.flag, layoutParams.type, layoutParams.format, layoutParams.gravity, layoutParams.width, layoutParams.height
	 */
	private void init(Context context) {
		imageView = new ImageView(context);
		final Drawable icon = mConfig.mIcon;
		mSize = mConfig.mSize;
		Util.setBackground(imageView, icon);
		addView(imageView, new ViewGroup.LayoutParams(mSize, mSize));
		initLayoutParams(context);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mRunner = new ScrollRunner(this);
		mVelocity = new MotionVelocityUtil(context);
	}

	private void initLayoutParams(Context context) {
		mLayoutParams = FloatBallUtil.getLayoutParams(context);
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == VISIBLE) {
			onConfigurationChanged(null);
		}
	}

	/**
	 * 将悬浮球附着到窗口（其实就是显示到窗口上）
	 */
	public void attachToWindow(WindowManager windowManager) {
		this.windowManager = windowManager;
		if (!isAdded) {
			windowManager.addView(this, mLayoutParams);
			isAdded = true;
		}
	}

	/**
	 * 将悬浮球从窗口移除。
	 */
	public void detachFromWindow(WindowManager windowManager) {
		this.windowManager = null;
		if (isAdded) {
			removeSleepRunnable();
			if (getContext() instanceof Activity) {
				windowManager.removeViewImmediate(this);
			} else {
				windowManager.removeView(this);
			}
			isAdded = false;
			sleep = false;
		}
	}

	/**
	 * 测量悬浮球大小，首次显示时直接移动到指定的位置。不是首次显示，则滑动到指定的位置
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = getMeasuredHeight();
		int width = getMeasuredWidth();

		int curX = mLayoutParams.x;
		if (sleep && curX != mSleepX && !mRunner.isRunning()) {
			sleep = false;
			postSleepRunnable();
		}
		if (mRunner.isRunning()) {
			mLayoutChanged = false;
		}
		if (height != 0 && isFirst || mLayoutChanged) {
			if (isFirst && height != 0) {
				location(width, height);
			} else {
				moveToEdge(false, sleep);
			}
			isFirst = false;
			mLayoutChanged = false;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		floatBallManager.floatballX = mLayoutParams.x;
		floatBallManager.floatballY = mLayoutParams.y;
	}

	/**
	 * 摆放悬浮球位置 x 方向在左边界或者右边界。y 方向在上边界，中间，或者下边界
	 */
	private void location(int width, int height) {
		FloatBallCfg.Gravity cfgGravity = mConfig.mGravity;
		mHideHalfLater = mConfig.mHideHalfLater;
		int gravity = cfgGravity.getGravity();
		int x;
		int y;
		int topLimit = 0;
		int bottomLimit = floatBallManager.mScreenHeight - height;
		int statusBarHeight = floatBallManager.getStatusBarHeight();
		//水平方向位于左边界或者右边界
		if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
			x = 0;
		} else {
			x = floatBallManager.mScreenWidth - width;
		}
		//竖直方向位于上边界或者下边界
		if ((gravity & Gravity.TOP) == Gravity.TOP) {
			y = topLimit;
		} else if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
			y = floatBallManager.mScreenHeight - height - statusBarHeight;
		} else {
			y = floatBallManager.mScreenHeight / 2 - height / 2 - statusBarHeight;
		}
		y = mConfig.mOffsetY != 0 ? y + mConfig.mOffsetY : y;
		if (y < 0) y = topLimit;
		if (y > bottomLimit)
			y = topLimit;
		if (isLocationServiceEnable) {
			LocationService locationService = ServiceAgency.getService(LocationService.class);
			int[] location = locationService.onRestoreLocation();
			if (location.length == 2) {
				int locationX = location[0];
				int locationY = location[1];
				if (locationX != -1 && locationY != -1) {
					onLocation(locationX, locationY);
					return;
				}
			}
		}
		onLocation(x, y);
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mLayoutChanged = true;
		floatBallManager.onConfigurationChanged(newConfig);
		moveToEdge(false, false);
		postSleepRunnable();
	}

	public void onLayoutChange() {
		mLayoutChanged = true;
		requestLayout();
	}

	/**
	 * 监听触摸事件。
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		mVelocity.acquireVelocityTracker(event);
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				touchDown(x, y);
				break;
			case MotionEvent.ACTION_MOVE:
				touchMove(x, y);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				touchUp();
				break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 按下事件，记录按下的坐标，移除悬浮球的睡眠状态（从只显示半边到全部显示）
	 */
	private void touchDown(int x, int y) {
		mDownX = x;
		mDownY = y;
		mLastX = mDownX;
		mLastY = mDownY;
		isClick = true;
		removeSleepRunnable();
	}

	/**
	 * 移动悬浮球，如果手指移动的距离超过了滑动的最小值，则判定为滑动而非点击。记录滑动的最新的 x，y。
	 */
	private void touchMove(int x, int y) {
		int totalDeltaX = x - mDownX;
		int totalDeltaY = y - mDownY;
		int deltaX = x - mLastX;
		int deltaY = y - mLastY;
		if (Math.abs(totalDeltaX) > mTouchSlop || Math.abs(totalDeltaY) > mTouchSlop) {
			isClick = false;
		}
		mLastX = x;
		mLastY = y;
		if (!isClick) {
			onMove(deltaX, deltaY);
		}
	}

	/**
	 * 抬起事件，触发滑动事件或者点击事件，滑动则是滑动到边界
	 */
	private void touchUp() {
		mVelocity.computeCurrentVelocity();
		mVelocityX = (int) mVelocity.getXVelocity();
		mVelocityY = (int) mVelocity.getYVelocity();
		mVelocity.releaseVelocityTracker();
		if (sleep) {
			wakeUp();
		} else {
			if (isClick) {
				onClick();
			} else {
				//滑动到边界
				moveToEdge(true, false);
			}
		}
		mVelocityX = 0;
		mVelocityY = 0;
	}

	/**
	 * 移动悬浮球到指定 x 坐标
	 * 计算悬浮球的 y 坐标：超过边界，则显示在边界处
	 * 计算需要滑动的距离
	 * 如果 smooth 为 true，则滑动到指定位置，
	 * 如果 smooth 为 false，则直接移动到指定位置，直接更新悬浮球的位置参数
	 */
	private void moveToX(boolean smooth, int destX) {
		int statusBarHeight = floatBallManager.getStatusBarHeight();
		final int screenHeight = floatBallManager.mScreenHeight - statusBarHeight;
		int height = getHeight();
		int destY = 0;
		if (mLayoutParams.y < 0) {
			destY = 0 - mLayoutParams.y;
		} else if (mLayoutParams.y > screenHeight - height) {
			destY = screenHeight - height - mLayoutParams.y;
		}
		if (smooth) {
			int dx = destX - mLayoutParams.x;
			int duration = getScrollDuration(Math.abs(dx));
			mRunner.start(dx, destY, duration);
		} else {
			onMove(destX - mLayoutParams.x, destY);
			postSleepRunnable();
		}
	}

	/**
	 * 唤醒悬浮球，移动悬浮球到左边界或者右边界
	 */
	private void wakeUp() {
		final int screenWidth = floatBallManager.mScreenWidth;
		int width = getWidth();
		int halfWidth = width / 2;
		int centerX = (screenWidth / 2 - halfWidth);
		int destX;
		destX = mLayoutParams.x < centerX ? 0 : screenWidth - width;
		sleep = false;
		moveToX(true, destX);
	}

	/**
	 * 移动悬浮球到左边界或者右边界。
	 * sleep 控制当悬浮球显示在边界时，是否只显示一半。
	 */
	private void moveToEdge(boolean smooth, boolean forceSleep) {
		final int screenWidth = floatBallManager.mScreenWidth;
		int width = getWidth();
		int halfWidth = width / 2;
		int centerX = (screenWidth / 2 - halfWidth);
		int destX;
		final int minVelocity = mVelocity.getMinVelocity();
		if (mLayoutParams.x < centerX) {
			sleep = forceSleep || Math.abs(mVelocityX) > minVelocity && mVelocityX < 0 || mLayoutParams.x < 0;
			destX = sleep ? -halfWidth : 0;
		} else {
			sleep = forceSleep || Math.abs(mVelocityX) > minVelocity && mVelocityX > 0 || mLayoutParams.x > screenWidth - width;
			destX = sleep ? screenWidth - halfWidth : screenWidth - width;
		}
		if (sleep) {
			mSleepX = destX;
		}
		moveToX(smooth, destX);
	}

	/**
	 * 计算滑动的时间间隔
	 */
	private int getScrollDuration(int distance) {
		return (int) (250 * (1.0f * distance / 800));
	}

	/**
	 * 移动指定的距离，无动画
	 */
	private void onMove(int deltaX, int deltaY) {
		mLayoutParams.x += deltaX;
		mLayoutParams.y += deltaY;
		if (windowManager != null) {
			windowManager.updateViewLayout(this, mLayoutParams);
		}
	}

	/**
	 * 移动到指定位置，无动画
	 */
	public void onLocation(int x, int y) {
		mLayoutParams.x = x;
		mLayoutParams.y = y;
		if (windowManager != null) {
			windowManager.updateViewLayout(this, mLayoutParams);
		}
	}

	public void onMove(int lastX, int lastY, int curX, int curY) {
		onMove(curX - lastX, curY - lastY);
	}

	@Override
	public void onDone() {
		postSleepRunnable();
		if (isLocationServiceEnable) {
			LocationService locationService = ServiceAgency.getService(LocationService.class);
			locationService.onLocationChanged(mLayoutParams.x, mLayoutParams.y);
		}
	}

	private void moveTo(int x, int y) {
		mLayoutParams.x += x - mLayoutParams.x;
		mLayoutParams.y += y - mLayoutParams.y;
		if (windowManager != null) {
			windowManager.updateViewLayout(this, mLayoutParams);
		}
	}

	public int getSize() {
		return mSize;
	}

	/**
	 * 触发点击事件
	 */
	private void onClick() {
		floatBallManager.floatballX = mLayoutParams.x;
		floatBallManager.floatballY = mLayoutParams.y;
		floatBallManager.onFloatBallClick();
	}

	private void removeSleepRunnable() {
		mSleepRunnable.removeSelf(this);
	}

	public void postSleepRunnable() {
		if (mHideHalfLater && !sleep && isAdded) {
			mSleepRunnable.postDelaySelf(this, 3000);
		}
	}
}
