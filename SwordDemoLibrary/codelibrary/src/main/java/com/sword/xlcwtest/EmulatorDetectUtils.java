package com.sword.xlcwtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.sword.FileUtils;
import com.sword.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 模拟器检测工具类
 */
public class EmulatorDetectUtils {

  private static final String TAG = "EmulatorDetectUtils";

  private void emulatorDetectUtilsTest(Context context) {
    String debugInfo = "\n\nEmulatorDetectUtils.isEmulator: " + EmulatorDetectUtils.isEmulator(context)
        /*+ "\n\n自买量 SDK, System.getOs: " + SystemUtil.getOS()*/;
    LogUtil.debug(debugInfo);


    String cpuinfo = FileUtils.readFileInfo("/proc/cpuinfo");
    LogUtil.debug("--------------cpuinfo--------------");
    LogUtil.debug(cpuinfo);
    LogUtil.debug("-----------------------------------");
  }

  /**
   * 获取最后判断结果，返回true为模拟器，返回false为真机
   */
  public static boolean isEmulator(Context ctx) {
    try {
      boolean resolveTelPhone = resolveTelPhone(ctx);
      boolean judgeRealSocCpuInfo = judgeRealSocCpuInfo();
      boolean judgeLightSensor = judgeLightSensor(ctx);
      boolean judgeEmulatorBuildInfo = judgeEmulatorBuildInfo(ctx);
      LogUtil.debug(TAG, "isEmulator >>"
          + " Build.MODEL: " + Build.MODEL
          + ", resolveTelPhone: " + resolveTelPhone
          + ", judgeRealSocCpuInfo: " + judgeRealSocCpuInfo
          + ", judgeLightSensor: " + judgeLightSensor
          + ", judgeEmulatorBuildInfo: " + judgeEmulatorBuildInfo);
      return !resolveTelPhone || !judgeRealSocCpuInfo
          || !judgeLightSensor || judgeEmulatorBuildInfo;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * 判断是否可以处理跳转到拨号的Intent
   *
   * @return true:真机 false:模拟器
   */
  private static boolean resolveTelPhone(Context ctx) {
    String url = "tel:" + "123456";
    Intent intent = new Intent();
    intent.setData(Uri.parse(url));
    intent.setAction(Intent.ACTION_DIAL);
    return intent.resolveActivity(ctx.getPackageManager()) != null;
  }

  /**
   * 判断是否是真机
   * 通过手机光传感器来判断
   * 不确定全部机型判断准确
   *
   * @return true:真机 false:模拟器
   */
  private static boolean judgeLightSensor(Context ctx) {
    try {
      SensorManager sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
      Sensor sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //光
      return sensor8 != null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * 判断真实处理平台信息
   *
   * @return true:真机 false:模拟器
   */
  private static boolean judgeRealSocCpuInfo() {
    boolean isRealSoc = true;
    File file = new File("/proc/cpuinfo");
    if (file.exists()) {
      int lineNum = 0;
      int lineNumWithHardware = 0;
      BufferedReader fileReader = null;
      try {
        fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String readLine;
        while ((readLine = fileReader.readLine()) != null) {
          lineNum++;

          LogUtil.debug(TAG, "cpuinfo-" + lineNum + ": " + readLine);

          if (readLine.contains("hardware")) {
            lineNumWithHardware = lineNum;
            if (readLine.contains("placeholder")) {
              isRealSoc = false;
              break;
            }
          } else if (readLine.contains("Revision")) {
            if (readLine.contains("000b")) {
              isRealSoc = false;
              break;
            }
          } else if (lineNumWithHardware - lineNum <= 2) {
            if (readLine.contains("Serial")) {
              if (readLine.contains("0000000000000001")) {
                // 针对BlueStacks 5.4.100.1026 N32 模拟器进行适配
                isRealSoc = false;
                break;
              } else if (readLine.contains("0000000000000000")) {
                // 针对 MuMu 模拟器进行适配
                isRealSoc = false;
                break;
              }
            }
          }
          
          //检查是否为 intel 或者 amd 的 CPU
          String lowerContent = readLine.toLowerCase();
          if (isIntelCpu(lowerContent) || isAmdCpu(lowerContent)) {
            isRealSoc = false;
            break;
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
        isRealSoc = false;
      } finally {
        if (fileReader != null) {
          try {
            fileReader.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

    return isRealSoc;
  }

  //Intel(R) Core(TM) i7-7700CPU@3.60GHz
  //core：酷睿；xeon：至强；pentium：奔腾；celeron：赛扬
  private static boolean isIntelCpu(String content) {
    //String intelRegion = "intel.*(core|xeon|pentium)";
    return content.contains("intel") && (content.contains("core") || content.contains("xeon") || content.contains("pentium") || content.contains("celeron"));
    /*String coreRegion = "core.*(i3|i5|i7|i9|G)-";
    String pentiumRegion = "pentium.*(g|s)-";
    String Xeon = "xeon.*w-";
    String celeron = "celeron.*(g|s)-";*/
  }


  // sempron：闪龙 ；phenom：奕龙；athlon：速龙；ryzen：锐龙；A4,A6,A8,A10,A12
  private static boolean isAmdCpu(String cpuinfo) {
    //String c = cpuinfo.toLowerCase();
    //String amdRegion = "amd.*(ryzon|athlon|fx|phenom|sempron|A\\d{0,1})";
    return cpuinfo.contains("amd") &&
        (cpuinfo.contains("ryzen")
            || cpuinfo.contains("athlon")
            || cpuinfo.contains("fx-")
            || cpuinfo.contains("phenom")
            || cpuinfo.contains("sempron")
            || cpuinfo.matches("a\\d{1,2}-"));
  }

  /**
   * 判断当前设备是否是真机
   *
   * @return true:模拟器 false:真机
   */
  @SuppressLint("HardwareIds")
  private static boolean judgeEmulatorBuildInfo(Context context) {
    return Build.FINGERPRINT.startsWith("generic")
        || Build.FINGERPRINT.toLowerCase().contains("vbox")
        || Build.FINGERPRINT.toLowerCase().contains("test-keys")
        || Build.MODEL.contains("google_sdk")
        || Build.MODEL.contains("Emulator")
//                || Build.SERIAL.equalsIgnoreCase("unknown") //MI 8 SE
        || Build.SERIAL.equalsIgnoreCase("android")
        || Build.MODEL.contains("Android SDK built for x86")
        || Build.MANUFACTURER.contains("Genymotion")
        || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
        || "google_sdk".equals(Build.PRODUCT)
        || ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
        .getNetworkOperatorName().equalsIgnoreCase("android");
  }

}
