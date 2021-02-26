package com.example.sworddemolibrary.java;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.sworddemolibrary.R;
import com.snail.antifake.deviceid.AndroidDeviceIMEIUtil;
import com.snail.antifake.deviceid.ShellAdbUtils;
import com.snail.antifake.jni.EmulatorDetectUtil;
import com.snail.antifake.jni.PropertiesGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity2 extends AppCompatActivity {
    String text2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

       /* TelephonyManagerJavaUtil.checkPermission(this);
        TelephonyManagerJavaUtil.debug(this);*/
        String text = "getOS: " + getOS();

        if (mayOnEmulatorViaQEMU(this)) text += "\n\n mayOnEmulatorViaQEMU"; else text += "\n\n mayNotOnEmulatorViaQEMU";
        if (isEmulatorViaBuild(this)) text += "\n\n isEmulatorViaBuild"; else text += "\n\n isNotEmulatorViaBuild";
        if (isEmulatorFromAbi()) text += "\n\n isEmulatorFromAbi"; else text += "\n\n isNotEmulatorFromAbi";
        if (isEmulatorFromCpu()) text += "\n\n isEmulatorFromCpu"; else text += "\n\n isNotEmulatorFromCpu";
        if (HasLightSensorManager()) text += "\n\n HasLightSensorManager"; else text += "\n\n NotHasLightSensorManager";
        if (checkIsNotRealPhone()) text += "\n\n checkIsNotRealPhone"; else text += "\n\n checkIsRealPhone";
        text = text + "\n\n " + text2;

        TextView textView = (TextView)findViewById(R.id.info);
        textView.setText(text);
    }

    public String getOS() {
        return HasLightSensorManager() && !checkIsNotRealPhone() && !mayOnEmulator(this) ? "1" : "3";
    }

    public boolean mayOnEmulator(Context context) {

        return mayOnEmulatorViaQEMU(context)
                || isEmulatorViaBuild(context)
                || isEmulatorFromAbi()
                || isEmulatorFromCpu();

    }


    public boolean HasLightSensorManager() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (sensor == null) {
            return false;
        }
        return true;
    }

    /**
     * 判断cpu是否为电脑来判断 模拟器
     *
     * @return true 为模拟器
     */
    public boolean checkIsNotRealPhone() {
        String cpuInfo = readCpuInfo();
        if ((cpuInfo.contains("intel") || cpuInfo.contains("amd"))) {
            return true;
        }
        return false;
    }

    public static String readCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ex) {
        }
        return result;
    }

    public static boolean mayOnEmulatorViaQEMU(Context context) {
        String qemu = PropertiesGet.getString("ro.kernel.qemu");
        return "1".equals(qemu);
    }

    public static boolean isEmulatorViaBuild(Context context) {

        if (!TextUtils.isEmpty(PropertiesGet.getString("ro.product.model"))
                && PropertiesGet.getString("ro.product.model").toLowerCase().contains("sdk")) {
            return true;
        }

        /**
         * ro.product.manufacturer likes unknown
         */
        if (!TextUtils.isEmpty(PropertiesGet.getString("ro.product.manufacturer"))
                && PropertiesGet.getString("ro.product.manufacture").toLowerCase().contains("unknown")) {
            return true;
        }

        /**
         * ro.product.device likes generic
         */
        if (!TextUtils.isEmpty(PropertiesGet.getString("ro.product.device"))
                && PropertiesGet.getString("ro.product.device").toLowerCase().contains("generic")) {
            return true;
        }

        return false;
    }

    private static boolean isEmulatorFromAbi() {

        String abi= getCpuAbi();

        return !TextUtils.isEmpty(abi) && abi.contains("x86");
    }

    // 查杀比较严格，放在最后，直接pass x86
    private boolean isEmulatorFromCpu() {
        ShellAdbUtils.CommandResult commandResult = ShellAdbUtils.execCommand("cat /proc/cpuinfo", false);
        String cpuInfo = commandResult == null ? "" : commandResult.successMsg;
        text2 = cpuInfo;
        return !TextUtils.isEmpty(cpuInfo) && ((cpuInfo.toLowerCase().contains("intel") || cpuInfo.toLowerCase().contains("amd")));
    }

    public static String getCpuAbi() {
        return PropertiesGet.getString("ro.product.cpu.abi");
    }
}