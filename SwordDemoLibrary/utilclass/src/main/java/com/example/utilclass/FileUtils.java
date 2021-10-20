package com.example.utilclass;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            path = Environment.getExternalStorageDirectory().getPath();
        } else {
            path = activity.getFilesDir().getPath();
        }

        path += "/" + PACKAGE_NAME + "_001";

        File dir = new File(path);
        Log.d("Sword", "存储目录：" + path);
    }

    /**
     * 获取应用包名
     *
     * @return
     */
    public static String getAppInfo(Context activity) {
        try {
            String pkName = activity.getPackageName();
            String versionName = activity.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = activity.getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
            return pkName + "_" + versionName + "_" + versionCode;
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        try{
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
