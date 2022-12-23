package com.sword;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataStorageUtils {
    
    
    public static String readFileInfo(String fileName) {
        StringBuilder fileInfo = new StringBuilder();

        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String message = null;
            while ((message = fileReader.readLine()) != null) {
                fileInfo.append(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileInfo.toString();
    }


    public static void getDataDir(Context activity) {
        String path = "";

        String PACKAGE_NAME = getAppInfo(activity);
        if (isSdCardExist()) {
            //获取外部存储的根目录
            path = Environment.getExternalStorageDirectory().getPath();
        } else {
            //获取内部存储中的应用私有目录
            path = activity.getFilesDir().getPath();
        }

        path += "/" + PACKAGE_NAME + "_001";

        File dir = new File(path);
        Log.d("Sword", "存储目录：" + path);
    }
    
    public static void getLogSwitchFile() {
        String logSwitch = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android" + File.separator;
        //String fileNameMd5 = Encryption.md5(logSwitch + "logSwitch");
    }

    /**
     * 获取应用包名
     */
    public static String getAppInfo(Context activity) {
        try {
            String pkName = activity.getPackageName();
            String versionName = activity.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = activity.getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
            return pkName + "_" + versionName + "_" + versionCode;
        } catch (Exception ignored) {
        }
        return null;
    }


    /**
     * 判断设备外部存储当前是否可以访问。（如果外部存储目录被挂在到了计算机上，应用程序将无法访问）
     */
    public static boolean isSdCardExist() {
        try{
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void fileCopy(String sourceFile, String aimFile) {

    }

    public static void fileCopy(File sourceFile, File aimFile) {

    }
}
