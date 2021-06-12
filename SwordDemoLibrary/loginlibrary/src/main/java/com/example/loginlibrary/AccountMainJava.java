package com.example.loginlibrary;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.Nullable;

public class AccountMainJava {

    public static void register(FragmentActivity activity) {

        //tokenLogin(activity);
        showDialogWithResId(activity, R.layout.xlcwsdk_visitor_switch_alert_dialog_layout);
    }

    public static void showDialogWithResId(FragmentActivity activity, int resId) {
        SwordDialogFragment dialogFragment = new SwordDialogFragment(resId, activity, initWindow(activity)/2, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogFragment.show(activity.getSupportFragmentManager(), "regisger_dialog");
    }

    //token 登录页面
    public static void tokenLogin(FragmentActivity activity) {
        SwordDialogFragment dialogFragment = new SwordDialogFragment(R.layout.xlcwsdk_token_login_dialog_layout, activity, initWindow(activity)/2, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogFragment.setResumeLitener(new SwordDialogFragment.ResumeLisenter() {
            @Override
            public void onResume(@Nullable Dialog dialog) {

               new SpinnerConfigure(activity, dialog.findViewById(R.id.spinner)).spinnerSample();
            }
        });
        dialogFragment.show(activity.getSupportFragmentManager(), "regisger_dialog");

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
