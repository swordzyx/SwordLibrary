package com.example.swordlibrary

import android.content.Context
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.util.Log

object TelephonyManagerUtil {
    val TAG = "zero_debug"

    fun debug(context: Context) {
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        Log.d(TAG, "运营商： " + telephony.simOperatorName)
        Log.d(
            TAG, "ProviderName: " + getProvidersName(
                context
            )
        )
    }

    fun getProvidersName(context: Context): String? {
        var ProvidersName = ""
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        // 返回唯一的用户ID;就是这张卡的编号神马的
        val operator = telephonyManager.simOperator

        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        ProvidersName += if (operator == null || operator.isEmpty()) {
            "4"
        } else if ("46000" == operator || "46002" == operator || "46004" == operator || "46007" == operator) //"中国移动"
        {
            "1"
        } else if (operator.startsWith("46001") || "46006" == operator || "46009" == operator) //"中国联通"
        {
            "3"
        } else if ("46003" == operator || "46005" == operator || "46011" == operator) //"中国电信"
        {
            "2"
        } else {
            "4"
        }
        return ProvidersName
    }

    fun getRawDeviceId(context: Context): String {
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

        return telephony.deviceId + "." + telephony.line1Number + "." + telephony.simSerialNumber + "." + telephony.subscriberId
    }

    /*fun checkPermission(context: Context) {
        if (context.checkSelfPermission())
    }*/
}