package com.sword.script;

import android.renderscript.ScriptGroup;

import com.google.android.material.badge.BadgeDrawable;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
    private static String decodeApkPath = "./script/file/B2_Dev_China2_Release_IL2CPP_xlcw_3.1.3_202107040723/";
    private String unZipTarget = "./script/file/unzip/";


    public static void main(String[] args) {
        //UseApkParseLib useApkParseLib = new UseApkParseLib(apkFilePath);
        //useApkParseLib.readPlatform_Properties();
        //useApkParseLib.readIcon();
        //useApkParseLib.readCertificationMd5();

        ChangeApkInfo changeApkInfo = new ChangeApkInfo();
        changeApkInfo.decodeApk(apkFilePath, decodeApkPath);
    }

    /**
     * 反编译 apk
     *
     * @param apkPath
     * @param output
     */
    private void decodeApk(String apkPath, String output) {
        try {
            File outputFile = new File(output);
            if (outputFile.exists()) {
                if (!outputFile.delete()) {
                    showAlert(output + " 删除失败");
                    return;
                }
            }

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
     * 将清单文件中的包名更改为指定的新包名
     *
     * @param newPackageName 新包名
     * @param oldPackageName 旧包名
     */
    private void changePackageNameInManifest(String oldPackageName, String newPackageName) {
        changeStringInFile(decodeApkPath + "AndroidManifest.xml", oldPackageName, newPackageName);
    }

    private void changePackageNameInSmali(String oldPackageName, String newPackageName) {
        String[] oldPackageFilesName = oldPackageName.split("\\.");
        for (String s : oldPackageFilesName) {
            showAlert("oldPackageFileNames: " + s);
        }

        String[] newPackageFilesName = newPackageName.split("\\.");
        for (String s : newPackageFilesName) {
            showAlert("newPackageFileNames: " + s);
        }

        if (oldPackageFilesName.length == newPackageFilesName.length) {
            String fileNameCache = "";
            for (int i = 0; i < oldPackageFilesName.length; i++) {
                if (!oldPackageFilesName[i].equals(newPackageFilesName[i])) {
                    File file = new File(decodeApkPath + "smali/" + fileNameCache + oldPackageFilesName[i]);
                    File destFile = new File(decodeApkPath + "smali/" + fileNameCache + newPackageFilesName[i]);
                    if (!file.renameTo(destFile)) {
                        showAlert(file.getPath() + " 重命名为 " + destFile.getPath() + " 失败");
                    } else {
                        fileNameCache = newPackageFilesName[i] + "/";
                    }
                }
            }
        } else {
            showAlert("无法重命名包名，请手动修改");
        }

    }

    /**
     * 移动新的 icon 到反编译之后的 apk 目录中
     * @param newResDir
     */
    private void changeIcon(String newResDir) {
        File file = new File(newResDir);

        if (!file.exists()) {
            showAlert(file.getPath() + " 不存在");
            return;
        }

        if (!file.isDirectory()) {
            showAlert("必须为目录");
            return;
        }

        for (File f : file.listFiles()) {
            String taragetResDirName = decodeApkPath + "res/" + f.getName();
            File targetResDir = new File(taragetResDirName);
            if (targetResDir.exists()) {
                if (!targetResDir.delete()) {
                    showAlert(taragetResDirName + "删除失败");
                    return;
                }
            }
            showAlert("移动 " + f.getPath() + " 到 " + targetResDir);
            if (!f.renameTo(targetResDir)) {
                showAlert(f.getPath() + " 移动失败");
                return;
            }
        }
        showAlert("图标更换完成");
    }

    private void changeAppNameByXmlReader(String oldAppName, String newAppName) {
        String stringsFileName = decodeApkPath + "res/values/strings.xml";

        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File(stringsFileName));
            List<Element> elements = document.getRootElement().elements("string");
            for (Element e : elements) {
                if (e.attributeValue("name").equals("app_name")) {
                    e.setText(newAppName);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void changeAppNameByFileReader(String oldAppName, String newAppName) {
        String stringsFileName = decodeApkPath + "/res/values/strings.xml";
        changeStringInFile(stringsFileName, oldAppName, newAppName);
    }

    private void changeStringInFile(String filePath, String oldString, String newString) {
        File file = new File(filePath);
        if (!file.exists()) {
            showAlert("文件不存在");
            return;
        }

        File tempFile = new File(decodeApkPath + "temp_" + file.getName());
        tempFile.deleteOnExit();
        if (!file.renameTo(tempFile)) {
            showAlert(file.getName() + " 重命名失败");
            return;
        }

        try {
            if (!file.createNewFile()) {
                showAlert(file.getName() + "创建失败");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile)));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {

                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(oldString)) {
                        showAlert("替换 " + line + " 中的 " + oldString + " 为 " + newString);
                        line = line.replace(oldString, newString);
                    }
                    writer.write(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (tempFile.exists()) {
            if (!tempFile.delete()) {
                showAlert(tempFile.getPath() + " 删除失败");
            }
        }
        showAlert(filePath + " 中的 " + oldString + " 替换完成");
    }

    private void showAlert(String info) {
        System.out.println(info);
    }

    /**
     * 修改 platform_config.properties 中的属性
     * @param newProperties
     */
    private void changeProperties(HashMap<String, String> newProperties) {
        String propertiesFile = decodeApkPath + "assets/platform_config.properties";

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertiesFile));

            for (String key : newProperties.keySet()) {
                properties.setProperty(key, newProperties.get(key));
            }
            properties.store(new FileOutputStream(propertiesFile), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void encodeApk() {
        List<String> args = new ArrayList<>();
        args.add("apktool");
        args.add("b");
        args.add(decodeApkPath);
        executCommand(args);
    }

    private void zipalign(String apkPath, String outputPath) {
        List<String> args = new ArrayList<>();
        args.add("zipalign");
        args.add("-p");
        args.add("-f");
        args.add("-v");
        args.add("4");
        args.add(apkPath);
        args.add(outputPath);
        executCommand(args);
    }

    private void signApk(String apkPath, String v1Enable, String v2Enable, String jksPath, String ksPass, String ksAlias, String ksAliasPass) {
        List<String> args = new ArrayList<>();
        args.add("java");
        args.add("-jar");
        args.add("apksigner");
        args.add("sign");
        //是否开启 V1 签名
        args.add("--v1-signing-enabled");
        args.add(v1Enable);
        //是否开启 V2 签名
        args.add("--v2-signing-enabled");
        args.add(v2Enable);
        //args.add("--v3-signing-enabled");  是否开启 V3 签名
        //args.add("--v4-signing-enabled");  是否开启 V4 签名
        args.add("--ks");
        args.add(jksPath);
        args.add("--key-pass");
        args.add("pass:" + ksPass);
        args.add("--ks-key-alias");
        args.add(ksAlias);
        args.add("--ks-pass");
        args.add("pass:" + ksAliasPass);
        args.add("--out");
        args.add(apkPath.replace(".apk", "signed.apk"));
        args.add(apkPath);
    }

    //签名验证
    private void verifyApkSign(String apkPath) {
        List<String> args = new ArrayList<>();
        args.add("java");
        args.add("-jar");
        args.add("apksigner.jar");
        args.add("verify");
        args.add("v");
        args.add("--print-certs");
        args.add(apkPath);
    }

    private void executCommand(List<String> args) {
        processBuilder.command(args);
        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (process == null) {
            showAlert(args.toString() + "执行失败");
            return ;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String info;
            while((info = reader.readLine()) != null) {
                showAlert(info);
            }

            while ((info = errorReader.readLine()) != null) {
                showAlert(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zipAlignApk(String apkPath, String outputPath) {
        ArrayList<String> args = new ArrayList<>();
        args.add("zipalign");
        args.add("-p");
        args.add("-f");
        args.add("-v");
        args.add("4");
        args.add(apkPath);
        args.add(outputPath);
        execCommand(args);
    }

    private void signApk(String apkPath, String outputPath) {

    }

    private void execCommand(List<String> args) {
        processBuilder.command(args);
        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (process == null) {
            showAlert(args.toString() + "执行失败");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String info;
            while((info = reader.readLine()) != null) {
                showAlert(info);
            }

            while ((info = errorReader.readLine()) != null) {
                showAlert(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
