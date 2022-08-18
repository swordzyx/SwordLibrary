package com.sword;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 参考：[CacheEmulatorChecker](https://github.com/happylishang/CacheEmulatorChecker)
 */
public class EmulatorCheck {
  private static final String TAG = "EmulatorCheck";
  public static String emulatorResult = "";

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public static boolean isEmulator(Context context) {
    boolean b1 = !resolveTelPhone(context);
    boolean b2 = !hasLightSensor(context);
    boolean b3 = checkEmulatorByCpuInfoFile();
    boolean b4 = judgeBuildInfo(context);
    boolean b5 = checkAppInstalled(context.getPackageManager(), "com.android.server.telecom");
    emulatorResult = "resolveTelPhone is Emulator: " + b1 + ", hasLightSensor is Emulator: : " + b2 + ", checkEmulatorByCpuInfoFile is Emulator: : " + b3 + ", judgeBuildInfo is Emulator: : " + b4 + ", check com.android.server.telecom exist: " + b5;
    LogUtil.debug(TAG, emulatorResult );
    return /*b1 || */b2 || b3 || b4 /*|| b5*/;
  }

  /**
   * 判断是否可以处理跳转到拨号的Intent
   */
  private static boolean resolveTelPhone(Context ctx) {
    String url = "tel:" + "123456";
    Intent intent = new Intent();
    intent.setData(Uri.parse(url));
    intent.setAction(Intent.ACTION_DIAL);
    return intent.resolveActivity(ctx.getPackageManager()) != null;
  }

  @SuppressLint("PackageManagerGetSignatures")
  private static boolean checkAppInstalled(PackageManager pm, String packageName) {
    LogUtil.debug(TAG, "checkAppInstalled, packageName: " + packageName);
    try {
      return pm.getPackageInfo(packageName, 0) != null;
    } catch (Exception exception) {
      exception.printStackTrace();
      LogUtil.debug("checkAppInstalled ");
      return false;
    }
  }

  /**
   * 判断是否是真机
   * 通过手机光传感器来判断
   * 不确定全部机型判断准确
   *
   * @return true:真机 false:模拟器
   */
  private static boolean hasLightSensor(Context context) {
    try {
      SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
      Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
      return sensor != null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * 判断设备的 cpu 是否为 intel 或者 amd，是则为模拟器
   */
  //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private static boolean checkEmulatorByCpuInfoFile() {
    File file = new File("/proc/cpuinfo");
    if (!file.exists()) {
      return true;
    }

    int lineNum = 0;
    int lineNumWithHardware = 0;
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
      String readLine;
      while ((readLine = fileReader.readLine()) != null) {
        lineNum++;
        
        String lowerContent = readLine.toLowerCase();
        LogUtil.debug(TAG, "cpu info, line " + lineNum + " - " + readLine);
        if (lowerContent.contains("intel")) {
          return true;
        }
        if (lowerContent.contains("amd")) {
          return true;
        }
        
        if (readLine.contains("hardware")) {
          lineNumWithHardware = lineNum;
          if (readLine.contains("placeholder")) {
            return true;
          }
        } else if (readLine.contains("Revision")) {
          if (readLine.contains("000b")) {
            return true;
          }
        } else if (lineNumWithHardware - lineNum <= 2) {
          if (readLine.contains("Serial")) {
            if (readLine.contains("0000000000000001")) {
              // 针对BlueStacks 5.4.100.1026 N32 模拟器进行适配
             return true;
            } else if(readLine.contains("0000000000000000")){
              // 针对 MuMu 模拟器进行适配
              return true;
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      return true;
    }

    return false;
  }

  /**
   * 通过 shell 命令获取 cpuinfo 文件的信息，以此判断是否为模拟器
   *
   * 与上面的 {@link EmulatorCheck#checkEmulatorByCpuInfoFile()} 是一致的。
   */
  private boolean checkEmulatorCatCpuInfoShell() {
    CommandResult result = ShellAdbUtil.execShellCommand(false, "cat /proc/cpuinfo");
    String cpuinfo = result.getSuccessString();
    if (cpuinfo.toLowerCase().contains("intel") || cpuinfo.toLowerCase().contains("amd")) {
      return true;
    }
    return false;
  }

  /**
   * 根据设备的 Device 属性（工业外观设计属性，一般是厂商名），MODEL（最用用户可见的产品名），MANUFACTURER（产品/硬件制造商），判断是否为模拟器。
   * 判断当前设备是否是真机
   * 
   * 判断本机代码的指令集是否为 x86，模拟器的指令集名称一般为 x86
   * @return true:模拟器 false:真机
   */
  @SuppressLint("HardwareIds")
  private static boolean judgeBuildInfo(Context context) {
    String fingerprint = Build.FINGERPRINT.toLowerCase();

    return fingerprint.startsWith("generic")
        || fingerprint.contains("vbox")
        || fingerprint.contains("test-keys")
        || Build.MODEL.contains("google_sdk")
        || Build.MODEL.contains("Emulator")
        || Build.MODEL.toLowerCase().contains("sdk")
        || Build.SERIAL.equalsIgnoreCase("android")
        || Build.MODEL.contains("Android SDK built for x86")
        || Build.MANUFACTURER.contains("Genymotion")
        || Build.MANUFACTURER.toLowerCase().contains("unknown")
        || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
        || "google_sdk".equals(Build.PRODUCT)
        || ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
        .getNetworkOperatorName().equalsIgnoreCase("android")
        || Build.CPU_ABI.contains("x86");
  }

  /*private static boolean mayOnEmulatorViaQEMU() {
    CommandResult result = ShellAdbUtil.execShellCommand(false, "getprop ro.kernel.qemu");

    String qemu = result.getSuccessString();
    return "1".equals(qemu);
  }*/


  
}
