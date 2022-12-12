package com.example.swordlibrary.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.LayoutRes;

import com.example.swordlibrary.R;
import com.sword.ScreenSize;


public class SwordDialog extends Dialog {
    private static final int HORIZONTAL_PADDING = (int) ScreenSize.dp(48);
    
    private ScrollView contentScrollView;
    private View view;
    private DialogClickListener dialogClickListener; 

    public SwordDialog(Context context) {
        super(context, R.style.MyDialog);
        ScreenSize.initWindowSize(context);
    }

    public static SwordDialog createLoadingDialog(Context context) {
        ProgressBar progressBar = new ProgressBar(context);
        return new SwordDialog(context).customView(progressBar);
    }

    public static SwordDialog createErrorDialog(Context context, String errorText) {
        return new SwordDialog(context).customView(new ErrorView(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout containerView = new FrameLayout(getContext());
        setContentView(containerView);
        
        contentScrollView = new ScrollView(getContext());
        containerView.addView(containerView);
        
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = window.getAttributes();
        
        int maxWidth = ScreenSize.getWindowWidth() - 2 * HORIZONTAL_PADDING;
        lp.width = Math.min(maxWidth, ScreenSize.getWindowWidth() / 5 * 3);
        window.setAttributes(lp);
        
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public SwordDialog clickListeners(int[] resIDs, DialogClickListener dialogClickListener) {
        this.dialogClickListener = dialogClickListener;
        for (int resID : resIDs) {
            View view = findViewById(resID);
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
        return this;
    }
    
    public SwordDialog setWidth(int width) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = width;
        window.setAttributes(lp);
        return this;
    }
    
    public SwordDialog setHeight(int height) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = height;
        window.setAttributes(lp);
        return this;
    }
    
    public SwordDialog setSize(int width, int height) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = width;
        lp.height = height;
        window.setAttributes(lp);
        return this;
    }

    public SwordDialog customView(View view) {
        if (this.view != null) {
            contentScrollView.removeView(this.view);
        }
        
        this.view = view;
        contentScrollView.addView(this.view);
        return this;
    }
    
    public SwordDialog customView(@LayoutRes int layoutResId) {
        return customView(LayoutInflater.from(getContext()).inflate(layoutResId, null));
    }

    public View getCustomView() {
        return view;
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dialogClickListener != null) {
                dialogClickListener.onDialogClick(v, SwordDialog.this);
            }
        }
    };

    interface DialogClickListener {
        void onDialogClick(View v, SwordDialog dialog);
    }
}
