package com.example.swordlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.swordlibrary.R;
import com.sword.ScreenSize;


public class ViewFactory {

    @SuppressLint("Range")
    public static int makeAtMostMeasureSpec() {
        return View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.AT_MOST);
    }

    public static int makeExactlyMeasureSpec(int value) {
        return View.MeasureSpec.makeMeasureSpec(value, View.MeasureSpec.EXACTLY);
    }

    public static EditText buildEditText(Context context) {
        EditText editText = new EditText(context);
        editText.setMaxLines(1);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        editText.setTextSize(Theme.getDimen(context, R.dimen.login_dialog_edit_text_size));
        editText.setHintTextColor(Theme.getColor(context, R.color.login_dialog_phone_hint));
        editText.setBackground(Theme.createRoundRectDrawable((int) ScreenSize.dp(5), Color.WHITE));
        editText.setPadding((int) ScreenSize.dp(8), (int) ScreenSize.dp(10), 0, (int) ScreenSize.dp(10));
        return editText;
    }

    public static TextView buildButton(Context context) {
        TextView nextStepButton = new TextView(context);
        nextStepButton.setEnabled(false);
        nextStepButton.setBackground(Theme.createButtonStateDrawable(Theme.getColor(context, R.color.color_orange), Theme.getColor(context, R.color.color_gray)));
        nextStepButton.setTextColor(Color.WHITE);
        nextStepButton.setTextSize(ScreenSize.dp(20));
        return nextStepButton;
    }

    public static TextView buildTitleTextView(Context context) {
        TextView titleView = new TextView(context);
        titleView.setTextSize((int) ScreenSize.dp(22));
        titleView.setTextColor(Theme.getColor(context, R.color.dialog_title_text_color));
        titleView.setTypeface(titleView.getTypeface(), Typeface.BOLD);
        return titleView;
    }

    public static TextView buildContentTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setTextSize(ScreenSize.dp(4));
        return textView;
    }
}

