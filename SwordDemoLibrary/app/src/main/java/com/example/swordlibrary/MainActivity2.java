package com.example.swordlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.window.java.layout.WindowInfoTrackerCallbackAdapter;
import androidx.window.layout.DisplayFeature;
import androidx.window.layout.FoldingFeature;
import androidx.window.layout.WindowInfoTracker;
import androidx.window.layout.WindowLayoutInfo;

import com.sword.LogUtil;
import com.sword.ScreenSize;
import com.sword.view.TextCheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.flow.FlowCollector;


public class MainActivity2 extends AppCompatActivity {
  private static final String TAG = "MainActivity";
  public static String CPUABI = null;

  private WindowInfoTrackerCallbackAdapter windowInfoTracker;
  private final LayoutStateChangeCallback layoutStateChangeCallback = new LayoutStateChangeCallback();

  class LayoutStateChangeCallback implements Consumer<WindowLayoutInfo> {

    @Override
    public void accept(WindowLayoutInfo windowLayoutInfo) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          LogUtil.debug(TAG, "LayoutStateChange: " + windowLayoutInfo);
          for (DisplayFeature df : windowLayoutInfo.getDisplayFeatures()) {
            if (df instanceof FoldingFeature) {
              LogUtil.debug(TAG, "Folding State");
              LogUtil.debug(TAG, "State: " + ((FoldingFeature) df).getState());
              LogUtil.debug(TAG, "OcclusionType: " + ((FoldingFeature) df).getOcclusionType());
              LogUtil.debug(TAG, "Orientation: " + ((FoldingFeature) df).getOrientation());
            }
          }
        }
      });
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    windowInfoTracker = new WindowInfoTrackerCallbackAdapter(WindowInfoTracker.getOrCreate(this));

    LogUtil.debug(TAG, "onCreate configuration: " + getResources().getConfiguration());

    LogUtil.debug(TAG, "--------------------- DisplayManager.getDisplays --------------------");
    DisplayManager dm = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
    for(Display d: dm.getDisplays()) {
      LogUtil.debug(TAG, d.toString());
    }    
    LogUtil.debug(TAG, "--------------------- DisplayManager.getDisplays --------------------");
  }

  @Override
  protected void onStart() {
    super.onStart();
    windowInfoTracker.addWindowLayoutInfoListener(this, Runnable::run, layoutStateChangeCallback);
  }

  public static boolean isFoldDisplay(Context context) {
    final String KEY = "config_lidControlsDisplayFold";
    int id = context.getResources().getIdentifier(KEY, "bool", "android");
    if (id > 0) {
      return context.getResources().getBoolean(id);
    }
    return false;
  }

  @Override
  public void onConfigurationChanged(@NonNull Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    LogUtil.debug(TAG, "MainActivity  onConfigurationChanged");
    LogUtil.debug(TAG, "newConfig: " + newConfig);
    
    LogUtil.debug(TAG, "newConfig width: " + ScreenSize.dp(newConfig.screenWidthDp) + ", height: " + ScreenSize.dp(newConfig.screenHeightDp));
  }
  

  @Override
  protected void onStop() {
    super.onStop();
    windowInfoTracker.removeWindowLayoutInfoListener(layoutStateChangeCallback);
  }

  /**
   * 生成uuid
   */
  public static String buildUUID() {
    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    uuid = uuid + System.currentTimeMillis();
    return uuid;
  }

  /**
   * 生成uuid
   */
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

  private void showGallery(boolean isCrop) {
    Intent intent = new Intent(this, AlbumActivity.class);
    intent.putExtra("crop_photo", isCrop);
    startActivity(intent);
  }

  public static void getCPUABI() {
    if (CPUABI == null) {
      try {
        String os_cpuabi = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream())).readLine();
        if (os_cpuabi.contains("x86")) {
          CPUABI = "x86";
        } else if (os_cpuabi.contains("armeabi-v7a")) {
          CPUABI = "armeabi-v7a";
        } else if (os_cpuabi.contains("arm64-v8a")) {
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