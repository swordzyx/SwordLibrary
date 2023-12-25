package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 获取cpu型号
 * 两个方法都是获取cpu型号,只是不同cpu运行的代码不同
 */
public class CpuUtil {
    private static final String TAG = "CpuUtil";

    public static String getCpuName() {
        FileReader fr = null;
        Scanner sc = null;

        String var6;
        try {
            fr = new FileReader("/proc/cpuinfo");
            sc = new Scanner(fr);
            String line = null;
            HashMap map = new HashMap();

            while (sc.hasNextLine() && (line = sc.nextLine()) != null) {
                if (line.length() != 0) {
                    String[] temp = line.split(":");
                    map.put(temp[0].trim(), temp[1].trim());
                }
            }

            var6 = (String) map.get("Hardware");

            return var6;
        } catch (ArrayIndexOutOfBoundsException var16) {
            var16.printStackTrace();
            var6 = Build.HARDWARE;
        } catch (FileNotFoundException var17) {
            var17.printStackTrace();
            var6 = Build.HARDWARE;
            return var6;
        } finally {
            if (sc != null) {
                sc.close();
            }

            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }

        }

        return var6;
    }

    /**
     * 获取cpu型号
     *
     * @return cpu型号
     */
    public static String getCpuModelName() {

        String cpuModel = getCpuModel();

        if (TextUtils.isEmpty(cpuModel)) {
            return "";
        }

        assert cpuModel != null;
        if (cpuModel.toLowerCase().endsWith("kirin")) {
            cpuModel = cpuModel.toLowerCase().replace("kirin", Build.HARDWARE);
        } else if (cpuModel.contains("SM6225")) {
            String featureCode = JavaFileIO.readFile("/sys/devices/soc0/feature_code");
            Log.d(TAG, "featureCode: " + featureCode);
            if("AB".equals(featureCode)) {
                cpuModel = cpuModel.replace("SM6225", "Snapdragon 680");
            } else if ("AD".equals(featureCode)) {
                cpuModel = cpuModel.replace("SM6225", "Snapdragon 685");
            }
        } else if (cpuModel.equals("pyramid") && Build.DEVICE.equals("pyramid") &&
                Build.MANUFACTURER.equals("HTC") && Build.PRODUCT.equals("htc_pyramid")) {
            cpuModel = "Snapdragon MSM8260(Cortex A8)";
        } else if (cpuModel.contains("Tegra 2 Development System")) {
            cpuModel = "NVIDIA Tegra 2(Dual-Core Cortex A9)";
        } else if (cpuModel.contains("OMAP4430")) {
            cpuModel = "TI OMAP4430(Cortex A9)";
        } else if (cpuModel.contains("OMAP4460")) {
            cpuModel = "TI OMAP4460(Cortex A9)";
        } else if (cpuModel.startsWith("MT6")) {
            cpuModel = CpuUtil.parseMT6ModelString(cpuModel);
        }
        Log.d(TAG, "getCPUModel >> " + cpuModel);
        return cpuModel;
    }

    private static String getCpuModel() {
        String cpuModel = "";
        try {
            if (Build.VERSION.SDK_INT >= 31) {
                cpuModel = Build.SOC_MANUFACTURER + " " + Build.SOC_MODEL;
            } else {
                cpuModel = readSystemProperties();
            }

            if (!TextUtils.isEmpty(cpuModel)) {
                return cpuModel;
            } 
            
            cpuModel = getCPUInfo().get("Hardware");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cpuModel;
    }

    private static Map<String, String> getCPUInfo() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));

        String str;

        Map<String, String> output = new HashMap<>();

        while ((str = br.readLine()) != null) {

            String[] data = str.split(":");

            if (data.length > 1) {

                String key = data[0].trim().replace(" ", "_");
                if (key.equals("model_name")) key = "cpu_model";

                output.put(key, data[1].trim());
            }

        }
        br.close();

        return output;
    }

    private static String readSystemProperties() {
        try {
            @SuppressLint("DiscouragedPrivateApi")
            Method getStringMethod = Build.class.getDeclaredMethod("getString", String.class);
            getStringMethod.setAccessible(true);

            String cpuMaufacturer = (String) (getStringMethod.invoke(null, "ro.soc.manufacturer"));
            String cpuModel = (String) (getStringMethod.invoke(null, "ro.soc.model"));

            if (!TextUtils.isEmpty(cpuModel) && !Build.UNKNOWN.equals(cpuModel)) {
                return cpuMaufacturer + " " + cpuModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private static String parseMT6ModelString(String cpuModel) {
        if (TextUtils.isEmpty(cpuModel) || !cpuModel.startsWith("MT6")) {
            return cpuModel;
        }
        if (cpuModel.matches("MT6771V/C|MT6771V/CM|MT6771V/C\\\\(ENG\\\\)|MT6771V/W|MT6771V/WM")) {
            cpuModel = "MediaTek Helio P60(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6771V/CT|MT6771V/WT")) {
            cpuModel = "MediaTek Helio P70(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6771V/CL|MT6771V/WL")) {
            cpuModel = "MediaTek Helio P70M(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6765V/WA|MT6765V/WB|MT6765V/CA|MT6765V/CB")) {
            cpuModel = "MediaTek Helio P35(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6765G")) {
            cpuModel = "MediaTek Helio G35(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6768V/W|MT6768V/WB|MT6768V/CA|MT6768V/CB")) {
            cpuModel = "MediaTek Helio P65(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6789U")) {
            cpuModel = "MediaTek Helio G99 Ultimate(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6789G")) {
            cpuModel = "MediaTek Helio G99 Ultra(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6789V/CD|MT6789")) {
            cpuModel = "MediaTek Helio G99(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6781V/CD|MT6781")) {
            cpuModel = "MediaTek Helio G96(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6785V/WU|MT6785V/WB|MT6785V/CU|MT6785V/CB")) {
            cpuModel = "MediaTek Helio G90(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6768V/WV|MT6768V/WC|MT6768V/CV|MT6768V/CC")) {
            cpuModel = "MediaTek Helio G90T(" + cpuModel + ")";
        } else if (cpuModel.matches ("MT6785V/WV|MT6785V/CW|MT6785V/WD|MT6785V/CD")) {
            cpuModel = "MediaTek Helio G95(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6769V/WZ|MT6769V/CZ|MT6769V/WY|MT6769V/CY")) {
            cpuModel = "MediaTek Helio G85(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6769V/WT|MT6769V/CT|MT6769V/WU|MT6769V/CU")) {
            cpuModel = "MediaTek Helio G80(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6769V/WA|MT6769V/CA|MT6769V/WB|MT6769V/CB")) {
            cpuModel = "MediaTek Helio G70(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6761V/WD|MT6761V/CD")) {
            cpuModel = "MediaTek Helio A20(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6761V/WA|MT6761V/WB|MT6761V/CA|MT6761V/CB")) {
            cpuModel = "MediaTek Helio A22(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6762V/WA|MT6762V/WB|MT6762V/CA|MT6762V/CB")) {
            cpuModel = "MediaTek Helio P22(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6762G")) {
            cpuModel = "MediaTek Helio G25(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6765H")) {
            cpuModel = "MediaTek Helio G37(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6762V/WDAMB-H")) {
            cpuModel = "MediaTek Helio A25(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6853V/ZA|MT6853V/NZA")) {
            cpuModel = "Dimensity 720(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6833V/ZA|MT6833V/NZA")) {
            cpuModel = "Dimensity 700(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6889Z/CZA")) {
            cpuModel = "Dimensity 1000+(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6885Z/CZA")) {
            cpuModel = "Dimensity 1000L(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6883Z/CZA")) {
            cpuModel = "Dimensity 1000C(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6891Z/CZA")) {
            cpuModel = "Dimensity 1100(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6893Z/CZA")) {
            cpuModel = "Dimensity 1200(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6893Z_C/CZA")) {
            cpuModel = "Dimensity 1200-vivo(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6893Z_D/CZ")) {
            cpuModel = "Dimensity 1200-Ultra(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6893Z_A/CZA")) {
            cpuModel = "Dimensity 1200-AI(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6877V/ZA")) {
            cpuModel = "Dimensity 900(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6877V/TZA")) {
            cpuModel = "Dimensity 920(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6877V/TTZA|MT6877TT｜MT6877")) {
            cpuModel = "Dimensity 1080(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6873V/ZA")) {
            cpuModel = "Dimensity 800(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6875V/TZA")) {
            cpuModel = "Dimensity 820(" + cpuModel + ")";
        } else if (cpuModel.matches("BRT-W09|MT8797")) {
            cpuModel = "Kompanio 1300T(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6893Z_Z/CZA")) {
            cpuModel = "Dimensity 1300(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6886V_A/CZA")) {
            cpuModel = "Dimensity 7200-Ultra(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6886V/CZA|MT6886")) {
            cpuModel = "Dimensity 7200(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6853V/TZA|MMT6853V/TNZA")) {
            cpuModel = "Kompanio 800U(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6895Z/CZ")) {
            cpuModel = "Dimensity D8000(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6895Z/TCZ|MT6895")) {
            cpuModel = "Dimensity D8100(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6896Z_A/CZA")) {
            cpuModel = "Dimensity 8200-Ultra(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6896Z/CZA|MT6896")) {
            cpuModel = "Dimensity 8200(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6895Z_A/CZA")) {
            cpuModel = "Dimensity 8000-MAX(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6895Z_A/TCZA")) {
            cpuModel = "Dimensity 8100-MAX(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6895Z_B/TCZA|MT6895T")) {
            cpuModel = "Dimensity 8100-Ultra(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6897Z_A/ZA")) {
            cpuModel = "Dimensity 8300-Ultra(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6897Z/ZA|MT6897")) {
            cpuModel = "Dimensity 8300(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6983W/CZA|MT6983Z/TCZA|MT6983T")) {
            cpuModel = "Dimensity 9000+(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6983")) {
            cpuModel = "Dimensity 9000(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6985W/CZA|MT6985")) {
            cpuModel = "Dimensity 9200(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6833")) {
            cpuModel = "Dimensity 6020(" + cpuModel + ")";
        } else if (cpuModel.matches("MT8771V_T/PZA|MT8771V_T/NZA")) {
            cpuModel = "Dimensity 6080(" + cpuModel + ")";
        } else if (cpuModel.matches("MT8791V_T/TZA|MT8791V_T/TNZA")) {
            cpuModel = "Dimensity 7050(" + cpuModel + ")";
        } else if (cpuModel.matches("MT8797Z_Z/CZA|MT8797Z_Z/CNZA")) {
            cpuModel = "Dimensity 8020(" + cpuModel + ")";
        } else if (cpuModel.matches("MT6985W/TCZA|MT6985T")) {
            cpuModel = "Dimensity 9200+(" + cpuModel + ")";
        } else if (cpuModel.matches("MT68cpuModel.matches(35V/ZA|MT6835)")){
            cpuModel = "Dimensity 6100+(" + cpuModel + ")";
        }
        return cpuModel;
    }
}
