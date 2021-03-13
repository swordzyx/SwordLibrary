package com.example.swordlibrary.java.androidapi;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiManagerUtil {

    public String testGetIpAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = manager.getConnectionInfo().getIpAddress();
        String ipString = (ipAddress & 0xff) + "." + (ipAddress>>8 & 0xff) + "." + (ipAddress>>16 & 0xff) + "." + (ipAddress >> 24 & 0xff);
        return ipString;
    }
}
