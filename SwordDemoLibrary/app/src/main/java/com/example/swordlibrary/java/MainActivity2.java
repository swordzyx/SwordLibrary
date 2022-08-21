package com.example.swordlibrary.java;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.swordlibrary.R;
import com.sword.xlcwtest.EmulatorDetectUtils;
import com.sword.xlcwtest.EmulatorDetectUtilsUpdate1;
import com.sword.LogUtil;
import com.sword.xlcwtest.EmulatorDetectUtilsUpdate2;
import com.sword.xlcwtest.SystemUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;


public class MainActivity2 extends AppCompatActivity {
    private static final String tag = "app-MainActivity";
    public static String CPUABI = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*String t = "check emulator: " + EmulatorCheck.isEmulator(this);
        LogUtil.debug(tag, t);
        t += "\n\n" + EmulatorCheck.emulatorResult;
        ((TextView)findViewById(R.id.text)).setText(t);*/
        String debugInfo = "\n\n修改后 EmulatorDetectUtilsUpdate-1.isEmulator: " + EmulatorDetectUtilsUpdate1.isEmulator(this) 
            + "\n\n修改后 EmulatorDetectUtilsUpdate-2.isEmulator: " + EmulatorDetectUtilsUpdate2.isEmulator(this)
            + "\n\n修改前 EmulatorDetectUtils.isEmulator: " + EmulatorDetectUtils.isEmulator(this)
            + "\n\n自买量 SDK, System.getOs: " + SystemUtil.getOS();
        
        LogUtil.debug(debugInfo);
    }

    /**
     * 生成uuid
     * */
    public static String buildUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        uuid = uuid + System.currentTimeMillis();
        return uuid;
    }

    /**
     * 生成uuid
     * */
    public static String buildUUID(String name) {
        String uuid = UUID.fromString(name).toString();
        uuid = uuid + System.currentTimeMillis();
        return uuid;
    }

    String s = "qwertyuiopasdfghjklzxcvbnmQWERRTYUIOPASDFGHJKLZXCVBNM1234567890";
    public String randomString(int count) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(s.charAt(random.nextInt(s.length())));
        }
        return result.toString();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Sword", "申请权限");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }


    public static void getCPUABI() {
        if (CPUABI == null) {
            try {
                String os_cpuabi = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream())).readLine();
                if (os_cpuabi.contains("x86")) {
                    CPUABI = "x86";
                } else if (os_cpuabi.contains("armeabi-v7a")) {
                    CPUABI = "armeabi-v7a";
                } else if (os_cpuabi.contains("arm64-v8a")){
                    CPUABI = "arm64-v8a";
                } else {
                    CPUABI = "armeabi";
                }
            } catch (Exception e) {
                CPUABI = "armeabi";
            }
            Log.d("sword", CPUABI);
        }
    }

    //打印 Local 信息
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public String getLocal() {
        Locale locale = Locale.getDefault();
        return "language: " + locale.getLanguage() + "\nCountry: " + locale.getCountry() + "\n languageTag: " + locale.toLanguageTag() + "\ndisplayCountry: " + locale.getDisplayCountry() + "\ngetDisplayLanguage: " + locale.getDisplayLanguage() + "\ngetDisplayName: " + locale.getDisplayName() + "\ngetDisplayScript: " + locale.getDisplayScript() + "\ngetDisplayVariant: " + locale.getDisplayVariant() + "\ngetISO3Country: " + locale.getISO3Country() + "\ngetISO3Language: " + locale.getISO3Language() + "\ngetScript: " + locale.getScript() + "\ngetVariant: " + locale.getVariant();

    }
    
    //获取 Android 设备所在地区
    public void getIpAddress() {

        //1. 通过 NetworkInterface 获取 IP 地址，可能会获取不到
        //2. 通过 TelephonyManager 获取类似 MCC 或者 SIM 卡运营商所在地区的国家/地区代码
        //3. 通过 WifiManager 获取 IP 地址
        //4. 由服务器端下发 IP 地址，通过 IpSeekUtils 获取 IP 地址所在地区/国家
        //5. 访问外部连接
        //      http://pv.sohu.com/cityjson
        //      http://pv.sohu.com/cityjson?ie=utf-8
        //      http://ip.chinaz.com/getip.aspx


        Log.d("TAG", "shell 获取外网 ip：" + ShellAdbUtil.execShellCommand(false, "curl ifconfig.me").getSuccessString());
    }


}