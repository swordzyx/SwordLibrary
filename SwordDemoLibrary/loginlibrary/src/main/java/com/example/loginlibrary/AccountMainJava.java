package com.example.loginlibrary;

import android.content.Context;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.util.Size;
import android.view.Display;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

public class AccountMainJava {

    public static void register(FragmentActivity activity) {
        SwordDialogFragment dialogFragment = new SwordDialogFragment(R.layout.xlcwsdk_user_register_dialog_layout, activity);
        dialogFragment.show(activity.getSupportFragmentManager(), "regisger_dialog");
    }

    public static int initWindow(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 获取屏幕宽高
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        return screenWidth;
    }




}
