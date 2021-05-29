package com.example.loginlibrary;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

public class AccountMainJava {

    public static void register(FragmentActivity activity) {

        SwordDialogFragment dialogFragment = new SwordDialogFragment(R.layout.xlcwsdk_delete_token_dialog_layout, activity, initWindow(activity)/2, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogFragment.show(activity.getSupportFragmentManager(), "regisger_dialog");
//        setSize(dialogFragment.requireDialog(), initWindow(activity)/2, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static int initWindow(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 获取屏幕宽高
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        return screenWidth;
    }


    public static void setSize(Dialog dialog, int width, int height) {
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = width;
        lp.height = height;
        dialog.getWindow().setAttributes(lp);
    }

}
