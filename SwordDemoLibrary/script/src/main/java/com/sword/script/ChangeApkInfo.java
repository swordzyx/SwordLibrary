package com.sword.script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;

import javax.xml.parsers.DocumentBuilderFactory;

public class ChangeApkInfo {
    ProcessBuilder processBuilder = new ProcessBuilder();
    //反编译：apktool d -o 九尾买量/jiuwei/ 九尾买量/Aggregated_v2.0.0_1_release_xlcw_20210413.apk
    //回编译：apktool.bat b 九尾买量/jiuwei
    private static final String apktoolsPath = "E:\\custombin";

    public static void main(String[] args) {

    }

    private void unzipApk(String apkPath, String output) {
        try {
            Process proc = processBuilder.command("apktool", "d", "-o", output, apkPath).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String info ;
            while((info = reader.readLine()) != null) {
                System.out.println(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("apk 反编译完成");
    }

    private void changePackageName(String xmlPath, String packageName) {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        InputStream input = null;
    }
}
