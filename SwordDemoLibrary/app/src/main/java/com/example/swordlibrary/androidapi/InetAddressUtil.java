package com.example.swordlibrary.androidapi;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.net.InetAddress;

/**
 * [InetAddress](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/InetAddress.html) 类的用法
 */
public class InetAddressUtil {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getIP() {
        return InetAddress.getLoopbackAddress().getHostAddress();
    }
}
