package com.sword.floatball;

import android.view.MotionEvent;

public class CustomGestureDetector {

	/*class GestureCallback extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
      if ((floatMenuVisiable() && touchOutofMenu(e)) || dragHelper.findTopChildUnder((int) e.getX(), (int) e.getY()) == floatBallView) {
        switchFloatMenuVisibility();
      }
      return true;
    }
  }*/
	
	public void processInterceptTouchEvent(MotionEvent event) {
		/*if (dragHelper.shouldInterceptTouchEvent(event)) {
      return true;
    }
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        downOutofMenu = touchOutofMenu(event) && floatMenuVisiable();
        LogUtil.debug("onInterceptTouchEvent touchOutofMenu: " + downOutofMenu);
        return downOutofMenu;
      case MotionEvent.ACTION_UP:
        if (downOutofMenu && touchOutofMenu(event) && floatMenuVisiable()) {
          LogUtil.debug("onInterceptTouchEvent switchFloatMenuVisibility");
          switchFloatMenuVisibility();
          return true;
        }
      default:
        break;
    }
    return false;*/
    /*View view  = dragHelper.findTopChildUnder((int) event.getX(), (int) event.getY());
    LogUtil.debug("find view , x : " + event.getX() + "-- y: " + event.getY() + "-- floatBallX: " + floatBallView.getX() + "-- floatBallY: " + floatBallView.getY() + " find status: " + (view != null));
    LogUtil.debug("shouldInterceptTouchEvent: " + dragHelper.shouldInterceptTouchEvent(event));
    return dragHelper.shouldInterceptTouchEvent(event);*/
	}

	public void processTouchEvent(MotionEvent event) {
		/*switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        downOutofMenu = touchOutofMenu(event);

        LogUtil.debug("onTouchEvent touchOutofMenu: " + downOutofMenu);
      case MotionEvent.ACTION_UP:
        if (downOutofMenu && touchOutofMenu(event) && floatMenuVisiable()) {
          LogUtil.debug("onTouchEvent switchFloatMenuVisibility");
          switchFloatMenuVisibility();
        }
      default:
        break;
    }*/

		/*if (event.getAction() == MotionEvent.ACTION_DOWN) {
      View view  = dragHelper.findTopChildUnder((int) event.getX(), (int) event.getY());
      LogUtil.debug("find view , x : " + event.getX() + "-- y: " + event.getY() + "-- floatBallX: " + floatBallView.getX() + "-- floatBallY: " + floatBallView.getY() + " find status: " + (view != null));
    }*/
	}
}
