package sword.view.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sword.ScreenSize;
import sword.logger.SwordLog;

/**
 * 悬浮菜单封装类
 */
class MenuItem {
    ImageView icon;
    TextView title;
    int verticalPadding = ScreenSize.dp(3);
    
    MenuItem(ImageView icon, TextView title) {
        this.icon = icon;
        this.title = title;
    }
    
    LinearLayout convertToLinearLayout(MenuItem item, Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        linearLayout.addView(item.icon, params);
        linearLayout.addView(item.title, params);
        return linearLayout;
    }
    
    public void setVerticalPadding(int verticalPadding) {
        this.verticalPadding = verticalPadding; 
    }

    void layout(int left, int top) {
        icon.layout(left, top, left + icon.getMeasuredWidth(), top + icon.getMeasuredWidth());

        int titleTop = top + icon.getMeasuredHeight() + verticalPadding;
        title.layout(left, titleTop, left + title.getMeasuredWidth(), titleTop + title.getMeasuredHeight());
        
        SwordLog.debug("FloatMenuView - title - top: " + title.getTop() + "; left: " + title.getLeft() + "; bottom: " + title.getBottom() + "; right: " + title.getRight());
        SwordLog.debug("FloatMenuView - icon - top: " + icon.getTop() + "; left: " + icon.getLeft() + "; bottom: " + icon.getBottom() + "; right: " + icon.getRight());
    }

    int getWidth() {
        return Math.max(icon.getMeasuredWidth(), title.getMeasuredWidth());
    }

    int getHeight() {
        return title.getMeasuredHeight() + icon.getMeasuredHeight() + 2 * verticalPadding;
    }

    @SuppressLint("Range")
    void measure() {
        int defaultSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.AT_MOST);
        if (icon == null || title == null) {
            return;
        }
        icon.measure(defaultSpec, defaultSpec);

        int titleWidthSpec = View.MeasureSpec.makeMeasureSpec(icon.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
        title.measure(titleWidthSpec, defaultSpec);
        SwordLog.verbose("FloatMenuView - icon width: " + icon.getMeasuredWidth() + "; height: " + icon.getMeasuredHeight());
    }
}