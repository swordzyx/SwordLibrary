package com.example.loginlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class VerifyCodeInputView extends AppCompatEditText{
    int gap;
    int textMaxCount;
    int borderColor;
    int borderSize;

    Paint paint;
    RectF rectF = new RectF();
    Paint.FontMetrics metrics;
    int textLength;
    int boxWidth;
    InputCompleteListener inputCompleteListener;


    public VerifyCodeInputView(@NonNull Context context) {
        super(context);
    }

    public VerifyCodeInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context, attrs);
        initPain();

        metrics = paint.getFontMetrics();
        textLength = getText() == null ? 0 : getText().length();

        setFilters(new InputFilter[]{new InputFilter.LengthFilter(textMaxCount)});
        setBackground(null);
    }

    void initPain() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(borderSize);

        paint.setTextSize(getTextSize());
        paint.setTextAlign(Paint.Align.CENTER);
    }

    void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VerifyCodeInputView, 0, 0);
        gap = typedArray.getInt(R.styleable.VerifyCodeInputView_gap, 20);
        textMaxCount = typedArray.getInt(R.styleable.VerifyCodeInputView_textMaxCount, 6);
        borderColor = typedArray.getColor(R.styleable.VerifyCodeInputView_borderColor, Color.GRAY);
        borderSize = typedArray.getInt(R.styleable.VerifyCodeInputView_borderSize, 8);

        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int availableWidth = w - getPaddingLeft() - getPaddingRight();
        checkGap(availableWidth);

        boxWidth = (availableWidth - gap * (textMaxCount-1) ) / textMaxCount;
    }

    void checkGap(int availableWidth) {
        if(gap < 0 || (gap * (textMaxCount - 1) >= availableWidth)) {
            Log.d("VerfyCodeInputView", "gap is invalid, set to 0");
            gap = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int start = getPaddingLeft();
        int top = getPaddingTop();
        int bottom = getHeight();

        for (int i=0; i<textMaxCount; i++) {
            int left = start + (boxWidth + gap) * i;
            rectF.set(left, top, left + boxWidth, bottom);
            paint.setColor(borderColor);
            canvas.drawLine(left, bottom, left + boxWidth, bottom, paint);

            if (i < textLength) {
                paint.setColor(getCurrentTextColor());
                canvas.drawText(String.valueOf(getText().charAt(i)), (rectF.left + rectF.right)/2, (rectF.top + rectF.bottom - metrics.ascent - metrics.descent) / 2, paint);
            }
        }

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        textLength = text.length();

        if (textLength == textMaxCount && inputCompleteListener != null) {
            inputCompleteListener.inputComplete();
        }
    }

    public void setInputCompleteListener(InputCompleteListener listener) {
        this.inputCompleteListener = listener;
    }

    public interface InputCompleteListener {
        void inputComplete() ;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
