package com.example.sworddemolibrary.java;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class TelephonyManagerJavaUtil {
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void checkPermission(Context context) {
        String[] permissions = new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS};
        if(context.checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED || context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || context.checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, permissions, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void debug(Context context) {
        try {
            Log.d("sword_debug", "getIMEI: " + getIMEI(context));
            Log.d("sword_debug", "getProviderName: " + getProvidersName(context));
//            Log.d("sword_debug", "getRawDeviceId: " + getRawDeviceId(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProvidersName(Context context) {
        String ProvidersName = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String operator = telephonyManager.getSimOperator();

        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (operator == null || operator.isEmpty()) {
            ProvidersName += "4";
        } else if ("46000" == operator || "46002" == operator || "46004" == operator || "46007" == operator) //"中国移动"
        {
            ProvidersName += "1";
        } else if (operator.startsWith("46001") || "46006" == operator || "46009" == operator) //"中国联通"
        {
            ProvidersName += "3";
        } else if ("46003" == operator || "46005" == operator || "46011" == operator) //"中国电信"
        {
            ProvidersName += "2";
        } else {
            ProvidersName += "4";
        }
        return ProvidersName;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getRawDeviceId(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        return telephony.getDeviceId() + "." + telephony.getLine1Number() + "." + telephony.getSimSerialNumber() + "." + telephony.getSubscriberId() + "." + wifiManager.getConnectionInfo().getMacAddress();
    }

    public static String getIMEI(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            return telephonyManager.getDeviceId();
        }
    }


}
