package com.sword.script;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnZipApk {
    //https://github.com/hsiafan/apk-parser
    private ArrayList<String> readFiles = new ArrayList<>();
    private String apkFilePath = null;

    public UnZipApk(String apkFilePath) {
        this.apkFilePath = apkFilePath;
    }

    /**
     * 使用解压 zip 的方式解压 apk
     *
     * 1. 直接解压出来的 apk 里面的清单文件是乱码，因为是直接写入字节，需要现将字节转成字符串，然后写入文件
     * 2. 如何读取 resources.arsc 文件中的内容
     * 3. 通过 Java 的 Properties 类可读取 platform_config.Properties 文件中的内容
     * @param apkFilePath
     * @param targetFile
     */
    private void unZipApk(String apkFilePath, String targetFile) {

        try {
            ZipFile zipFile = new ZipFile(apkFilePath);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                System.out.println("unzip: " + zipEntry.getName());
                if (readFiles.contains(zipEntry.getName())) {
                    File f = new File(targetFile + zipEntry.getName());
                    if (f.getParentFile() != null && !f.getParentFile().exists()) {
                        if (!f.getParentFile().mkdir()) {
                            System.out.println(f.getParent() + " 创建失败");
                            return;
                        }
                    }
                    if (!f.exists()) {
                        if (!f.createNewFile()) {
                            System.out.println(f.getName() + " 创建失败");
                            return;
                        }
                    }

                    try(BufferedInputStream input = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(f))
                    ) {
                        int count = -1;
                        byte[] b = new byte[1024];
                        while ((count = input.read(b)) != -1) {
                            output.write(b, 0, count);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过 AndroidManifest 读取 Apk 信息
     * @param manifestPath
     */
    private void getApkInfo(String manifestPath) {
        SAXReader reader = new SAXReader();
        try {
            ApkInfo apkInfo = new ApkInfo();
            Document document = reader.read(new File(manifestPath));
            Element root = document.getRootElement();
            System.out.println(root.getName());
            String packageName = root.attributeValue("package");
            apkInfo.setPackageName(packageName);
            apkInfo.setApkFileName(apkFilePath);
            String appName = root.element("application").attributeValue("android:label");
            System.out.println(appName);
            //apkInfo.setAppName();
            //apkInfo.setCode();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
