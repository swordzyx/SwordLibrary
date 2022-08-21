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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class EmulatorDetectUtilsUpdate1 {

    private static final String TAG = "EmulatorDetectUtils第一版修改";

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
                || !judgeCpuInfo2
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
        // Android 11 开始，查找其他应用包信息需要在清单文件中申明 <query> 标签，否则 resolveActivity 会返回 null
        // 因此如果当前运行设备的 Android 版本为 11 及以上，均视为可跳转到拨号应用，防止将 Android 11 及以上的设备判定为模拟器。
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            return true;
        }
        
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
            SensorManager sensorManager = (SensorManager) ctx.getSystemService(SENSOR_SERVICE);
            Sensor sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //光
            return sensor8 != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     *
     * @param filePath  "/proc/cpuinfo"
     * @return filePathContent
     */
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

        LogUtil.verbose(TAG, "readSystemFileContent >> filePath:" + filePath + " filePathContent:" + filePathContent);

        return filePathContent.toString();
    }

    private static boolean judgeCpuInfo2() {
        boolean flag = true;
        String[] args = {"/proc/cpuinfo"};

        for(String filePath : args){
            //todo: 是否可以边读取 cpuinfo 文件，边判断是否为模拟器，一旦检测到为模拟器，则直接返回，而不必将整个 cpuinfo 文件的内容先存到 String 字符串中，然后在判断这个字符串中的内容
            String filePathContent = readSystemFileContent(filePath);
            if (filePathContent == null || TextUtils.isEmpty(filePathContent)) {
                continue;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(filePathContent.getBytes())));

            try {
                String readLine;
                int lineNum = 0;
                int lineNumWithHardware = -1;
                while ((readLine = br.readLine()) != null) {
                    lineNum++;
                    if (readLine.contains("Hardware")) {
                        lineNumWithHardware = lineNum;
                        if (readLine.contains("placeholder")) {
                            flag = false;
                        }
                    } else if (readLine.contains("Revision")) {
                        if (readLine.contains("000b")) {
                            flag = false;
                        }
                    } else if (lineNumWithHardware - lineNum <= 2) {
                        if (readLine.contains("Serial")) {
                            if (readLine.contains("0000000000000001")) {
                                // 针对BlueStacks 5.4.100.1026 N32 模拟器进行适配
                                flag = false;
                            } else if(readLine.contains("0000000000000000")){
                                // 针对MuMu模拟器进行适配
                                flag = false;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
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
