package sword.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swordlibrary.R;
import com.sword.ScreenSize;


public class ErrorView extends ViewGroup {
    private final int iconMarginRight = (int) ScreenSize.dp(5);
    private final int errorTextMarginTop = (int) ScreenSize.dp(10);
    private final int errorTextMarginBottom = (int) ScreenSize.dp(10);


    private final ImageView imageView;
    private final TextView errorTextView;

    public ErrorView(Context context) {
        super(context);

        imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.error_alert_icon);
        addView(imageView);

        errorTextView = new TextView(context);
        errorTextView.setTextSize(ScreenSize.dp(16));
        errorTextView.setTextColor(Color.BLACK);
        errorTextView.setGravity(Gravity.CENTER);
        addView(errorTextView);

        setBackground(Theme.getDrawableByResId(context, R.drawable.error_alert_background));
        setPadding(getPaddingLeft() + (int) ScreenSize.dp(30), getPaddingTop(), getPaddingRight() + (int) ScreenSize.dp(30), getPaddingBottom());
    }

    public void setErrorText(String errorText) {
        errorTextView.setText(errorText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        imageView.measure(
                ViewFactory.makeExactlyMeasureSpec((int) ScreenSize.dp(15)),
                ViewFactory.makeExactlyMeasureSpec((int) ScreenSize.dp(15))
        );
        errorTextView.measure(
                ViewFactory.makeAtMostMeasureSpec(),
                ViewFactory.makeAtMostMeasureSpec()
        );

        setMeasuredDimension(
                getPaddingLeft() + imageView.getWidth() + iconMarginRight + errorTextView.getWidth() + getPaddingRight(),
                getPaddingTop() + Math.max(imageView.getHeight() , errorTextView.getHeight() + errorTextMarginTop + errorTextMarginBottom) + getPaddingBottom()
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        imageView.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + imageView.getWidth(), getPaddingTop() + imageView.getHeight());
        errorTextView.layout(
                imageView.getRight() + iconMarginRight,
                getPaddingTop() + errorTextMarginTop,
                imageView.getRight() + iconMarginRight + errorTextView.getWidth(),
                getPaddingBottom()
        );
    }
}
