package com.sword.xlcwtest;

import static android.content.Context.SENSOR_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.sword.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class EmulatorDetectUtilsUpdate2 {

    private static final String TAG = "EmulatorDetectUtils";

    /**
     * 获取最后判断结果，返回true为虚拟机，返回false为真机
     */
    public static boolean isEmulator(Context ctx) {
        try {
            boolean resolveTelPhone = resolveTelPhone(ctx);
            boolean judgeCpuInfo2 = judgeCpuInfo2();
            boolean judgeLightSensor = judgeLightSensor(ctx);
            boolean judgeBuildInfo = judgeBuildInfo(ctx);
            LogUtil.debug(TAG, "isEmulator >>"
                + ", Build.MODEL: " + Build.MODEL
                + ", resolveTelPhone: " + resolveTelPhone
                + ", judgeCpuInfo2: " + judgeCpuInfo2
                + ", judgeLightSensor: " + judgeLightSensor
                + ", judgeBuildInfo: " + judgeBuildInfo);
            return !resolveTelPhone
                || judgeCpuInfo2
                || !judgeLightSensor
                || judgeBuildInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
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

    /**
     * 判断是否是真机
     * 通过手机光传感器来判断
     * 不确定全部机型判断准确
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

    /*
     * @param filePath  "/proc/cpuinfo"
     * @return filePathContent */
    private static String readSystemFileContent(String filePath) {
        if(TextUtils.isEmpty(filePath)){
            return null;
        }
        BufferedReader reader = null;
        StringBuilder filePathContent = new StringBuilder();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String text;
            while ((text = reader.readLine()) != null) {
                filePathContent.append(text).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        XlcwLog.privateV(TAG, "readSystemFileContent >> filePath:" + filePath + " filePathContent:" + filePathContent);

        return filePathContent.toString();
    }

    private static boolean judgeCpuInfo2() {
        File file = new File("/proc/cpuinfo");
        boolean isEmulator = false;

        if (!file.exists()) {
            isEmulator = true;
        }

        int lineNum = 0;
        int lineNumWithHardware = 0;
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String readLine;
            while ((readLine = fileReader.readLine()) != null) {
                lineNum++;

                String lowerContent = readLine.toLowerCase();
                LogUtil.verbose(TAG, "cpu info, line " + lineNum + " - " + readLine);
                if (lowerContent.contains("intel")) {
                    isEmulator = true;
                    break;
                }
                if (lowerContent.contains("amd")) {
                    isEmulator = true;
                    break;
                }

                if (readLine.contains("hardware")) {
                    lineNumWithHardware = lineNum;
                    if (readLine.contains("placeholder")) {
                        isEmulator = true;
                        break;
                    }
                } else if (readLine.contains("Revision")) {
                    if (readLine.contains("000b")) {
                        isEmulator = true;
                        break;
                    }
                } else if (lineNumWithHardware - lineNum <= 2) {
                    if (readLine.contains("Serial")) {
                        if (readLine.contains("0000000000000001")) {
                            // 针对BlueStacks 5.4.100.1026 N32 模拟器进行适配
                            isEmulator = true;
                            break;
                        } else if(readLine.contains("0000000000000000")){
                            // 针对 MuMu 模拟器进行适配
                            isEmulator = true;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            isEmulator = true;
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isEmulator;
    }

    /**
     * 判断当前设备是否是真机
     * @return true:模拟器 false:真机
     */
    @SuppressLint("HardwareIds")
    private static boolean judgeBuildInfo(Context context) {
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
