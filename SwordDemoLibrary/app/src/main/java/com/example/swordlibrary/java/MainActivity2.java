package com.example.swordlibrary.java;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.loginlibrary.AccountMain;
import com.example.loginlibrary.AccountMainJava;
import com.example.swordlibrary.R;
import com.example.swordlibrary.java.androidapi.TelephonyManagerJavaUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;


public class MainActivity2 extends AppCompatActivity {
    String text2 = "";
    TextView textView = null;
    public static String CPUABI = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FileUtils.getDataDir(this);

        //AccountMainJava.register(this);

        /*AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(getLayoutInflater().inflate(R.layout.sample, null))
                .create();
        dialog.show();*/


        //requestPermission();
        Log.d("Sword", randomString(14));
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

    //模拟器检测测试
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void EmulatorCheckTest() {
        String text = "getOS: " + (EmulatorCheck.mayOnEmulator(this) ? "1" : "3") ;
        text2 = EmulatorCheck.debug(this);

        text = text + "\n\n " + text2;
    }

    //TelephonyManager 信息打印测试
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void TelephonyManagerJavaUtiltest() {
        TelephonyManagerJavaUtil.checkPermission(this);
        TelephonyManagerJavaUtil.debug(this);
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


    //获取屏幕宽高
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics metrics = windowManager.getCurrentWindowMetrics();
        final WindowInsets windowInsets = metrics.getWindowInsets();
        Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());

        int insetsWidth = insets.right + insets.left;
        int insetsHeight = insets.top + insets.bottom;

        final Rect bounds = metrics.getBounds();
        final Size legacySize = new Size(bounds.width() - insetsWidth, bounds.height() - insetsHeight);

    }

    private void initWindow(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 获取屏幕宽高
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        if (screenWidth < screenHeight) {
            int i = screenWidth;
            screenWidth = screenHeight;
            screenHeight = i;
        }
    }


}