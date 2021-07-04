package com.sword.script;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;

public class ChangeApkInfo {
    //apktool 下载及安装：https://ibotpeaches.github.io/Apktool/install/
    private ProcessBuilder processBuilder = new ProcessBuilder();
    //反编译：apktool d -o 九尾买量/jiuwei/ 九尾买量/Aggregated_v2.0.0_1_release_xlcw_20210413.apk
    //回编译：apktool.bat b 九尾买量/jiuwei
    private static final String apktoolsPath = "E:\\custombin";
    private static String apkFilePath = "./script/file/B2_Dev_China2_Release_IL2CPP_xlcw_3.1.3_202107040723.apk";
    private String decodeApkPath = "./script/file/B2_Dev_China2_Release_IL2CPP_xlcw_3.1.3_202107040723/";
    private String unZipTarget = "./script/file/unzip/";


    public static void main(String[] args) {
        UseApkParseLib useApkParseLib = new UseApkParseLib(apkFilePath);
        //useApkParseLib.readPlatform_Properties();
        //useApkParseLib.readIcon();
        useApkParseLib.readCertificationMd5();

        /*ChangeApkInfo changeApkInfo = new ChangeApkInfo();
        changeApkInfo.readFiles.add("resources.arsc");
        changeApkInfo.readFiles.add("AndroidManifest.xml");
        changeApkInfo.readFiles.add("assets/platform_config.properties");*/

        //changeApkInfo.unzipApk(changeApkInfo.apkFilePath, changeApkInfo.decodeApkPath);

        //changeApkInfo.unZipApk(changeApkInfo.apkFilePath, changeApkInfo.unZipTarget);
        //changeApkInfo.getApkInfo(changeApkInfo.decodeApkPath + "AndroidManifest.xml");


    }

    /**
     * 反编译 apk
     * @param apkPath
     * @param output
     */
    private void decodeApk(String apkPath, String output) {
        try {
            Process proc = processBuilder.command("apktool", "d", "-o", output, apkPath).start();
            processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String info;
            while ((info = reader.readLine()) != null) {
                System.out.println(info);
            }

            BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String errorInfo = null;
            while ((errorInfo = error.readLine()) != null) {
                System.out.println(errorInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("apk 反编译完成");
    }





    /**
     * 修改包名
     * @param xmlPath
     * @param packageName
     */
    private void changePackageName(String xmlPath, String packageName) {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        InputStream input = null;
    }
}
