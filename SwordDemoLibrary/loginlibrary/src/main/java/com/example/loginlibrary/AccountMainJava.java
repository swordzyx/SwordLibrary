package com.example.loginlibrary;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

public class AccountMainJava {

    public static void register(FragmentActivity activity) {

        SwordDialogFragment dialogFragment = new SwordDialogFragment(R.layout.xlsdk_input_verify_code_dialog_layout, activity);
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
