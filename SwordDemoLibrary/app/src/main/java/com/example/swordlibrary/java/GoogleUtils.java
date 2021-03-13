package com.example.swordlibrary.java;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


/**
 * Google API：https://developers.google.com/android/reference/com/google/android/gms/common/GoogleApiAvailability
 */
public class GoogleUtils {
    /**
     * 检测是否包含 Google 服务
     */
    public static void checkGoogleService(Context context) {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (code == ConnectionResult.SUCCESS) {
            //支持 Google 服务
        } else {
            if (context instanceof Activity) {
                //尝试通过 Google Play 安装 Google 服务。
                GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable((Activity) context);

                //Google 服务安装失败，显示错误信息
                if (GoogleApiAvailability.getInstance().isUserResolvableError(code)) {
                    GoogleApiAvailability.getInstance().getErrorDialog((Activity)context, code,200).show();
                }
            }
        }
    }
}
