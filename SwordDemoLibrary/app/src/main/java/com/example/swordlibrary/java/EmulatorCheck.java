package com.example.swordlibrary.java;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

/**
 * 参考：[CacheEmulatorChecker](https://github.com/happylishang/CacheEmulatorChecker)
 */
public class EmulatorCheck {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String debug(Context context) {
        String text = "";
        if (mayOnEmulatorViaQEMU()) text += "\n\n mayOnEmulatorViaQEMU"; else text += "\n\n mayNotOnEmulatorViaQEMU";
        if (isEmulatorViaBuild()) text += "\n\n isEmulatorViaBuild"; else text += "\n\n isNotEmulatorViaBuild";
        if (isEmulatorFromAbi()) text += "\n\n isEmulatorFromAbi"; else text += "\n\n isNotEmulatorFromAbi";
        if (checkEmulatorCpuByFile()) text += "\n\n isEmulatorFromCpu"; else text += "\n\n isNotEmulatorFromCpu";
        if (hasLightSensor(context)) text += "\n\n HasLightSensorManager"; else text += "\n\n NotHasLightSensorManager";
        text += "\n\n ABI" + Build.CPU_ABI;
        return text;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean mayOnEmulator(Context context) {

        return mayOnEmulatorViaQEMU()
                || isEmulatorViaBuild()
                || isEmulatorFromAbi()
                || checkEmulatorCpuByFile()
                || !hasLightSensor(context);

    }
    /**
     * 通过 shell 命令获取 cpuinfo 文件的信息，以此判断是否为模拟器
     * @return
     */
    public boolean checkEmulatorCpuByShell() {
        ShellAdbUtil.CommandResult result = ShellAdbUtil.execShellCommand(false, "cat /proc/cpuinfo");
        String cpuinfo = result.getSuccessString();
        if (cpuinfo.toLowerCase().contains("intel") || cpuinfo.toLowerCase().contains("amd")) {
            return true;
        }
        return false;
    }

    /**
     * 判断设备的 cpu 是否为 intel 或者 amd，是则为模拟器
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean checkEmulatorCpuByFile() {
        String cpuinfo = FileUtils.readFileInfo("/proc/cpuinfo");
        if (!TextUtils.isEmpty(cpuinfo) && (cpuinfo.toLowerCase().contains("intel") || cpuinfo.toLowerCase().contains("amd"))) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否有光传感器，没有则为模拟器
     * @param context
     * @return
     */
    private static boolean hasLightSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (sensor != null) {
            return true;
        }
        return false;
    }

    public static boolean mayOnEmulatorViaQEMU() {
        ShellAdbUtil.CommandResult result = ShellAdbUtil.execShellCommand(false, "getprop ro.kernel.qemu");

        String qemu = result.getSuccessString();
        return "1".equals(qemu);
    }

    /**
     * 根据设备的 Device 属性（工业外观设计属性，一般是厂商名），MODEL（最用用户可见的产品名），MANUFACTURER（产品/硬件制造商），判断是否为模拟器。
     * @return
     */
    public static boolean isEmulatorViaBuild() {

        if (Build.MODEL.toLowerCase().contains("sdk")) {
            return true;
        }

        if (Build.MANUFACTURER.toLowerCase().contains("unknown")) {
            return true;
        }

        if (Build.DEVICE.toLowerCase().contains("generic")) {
            return true;
        }

        return false;
    }

    /**
     * 判断本机代码的指令集是否为 x86，模拟器的指令集名称一般为 x86
     * @return
     */
    private static boolean isEmulatorFromAbi() {
        return Build.CPU_ABI.contains("x86");
    }
}
