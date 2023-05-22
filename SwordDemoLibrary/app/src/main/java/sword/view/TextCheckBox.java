package sword.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.sword.ScreenSize;
import com.sword.codelibrary.R;

public class TextCheckBox extends AppCompatTextView implements Checkable {
    private boolean checked = false;

    private final Drawable checkDrawable;
    private final Drawable unCheckDrawable;
    private final int checkBoxSize = ScreenSize.dp(17);

    public TextCheckBox(@NonNull Context context) {
        this(context, null);
    }

    public TextCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        
        int checkBoxSizeMarginRight = ScreenSize.dp(5);
        setPadding(getPaddingLeft() + checkBoxSize + checkBoxSizeMarginRight, getPaddingTop(), getPaddingRight(), getPaddingBottom());

        int checkBoxMarginTop = ScreenSize.dp(2);
        checkDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.checkbox_checked, null);
        if (checkDrawable != null) {
            checkDrawable.setBounds(getPaddingLeft() - checkBoxSize - checkBoxSizeMarginRight, 0, getPaddingLeft() - checkBoxSizeMarginRight, checkBoxSize);
        }

        unCheckDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.checkbox_unchecked, null);
        if (unCheckDrawable != null) {
            unCheckDrawable.setBounds(getPaddingLeft() - checkBoxSize - checkBoxSizeMarginRight, 0, getPaddingLeft() - checkBoxSizeMarginRight, checkBoxSize);
        }
        
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (checked) {
            if(checkDrawable != null) {
                checkDrawable.draw(canvas);
            }
        } else {
            if (unCheckDrawable != null) {
                unCheckDrawable.draw(canvas);
            }
        }
        
        super.onDraw(canvas);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        invalidate();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}
