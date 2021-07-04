package com.sword.script;

import android.icu.util.Output;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.ApkSigner;
import net.dongliu.apk.parser.bean.ApkV2Signer;
import net.dongliu.apk.parser.bean.CertificateMeta;
import net.dongliu.apk.parser.bean.IconFace;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.sql.SQLOutput;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class UseApkParseLib {

    ApkFile apkFile = null;
    private String cacheFilePath = "./script/file/cache/";

    public UseApkParseLib(String apkFilePath) {
        try {
            apkFile = new ApkFile(apkFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readApkInfo() {
        ApkMeta meta = null;
        try {
            meta = apkFile.getApkMeta();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String metaName = meta.getName();
        String packageName = meta.getPackageName();
        String sdkVersion = meta.getCompileSdkVersion();
        String appName = meta.getLabel();
        String minSdkVersion = meta.getMinSdkVersion();
        Long versionCode = meta.getVersionCode();
        String versionName = meta.getVersionName();
        String targetSdkVersion = meta.getTargetSdkVersion();

        StringBuilder builder = new StringBuilder();
        builder.append("metaName: ").append(metaName).append("\npackageName: ").append(packageName).append("\nsdkVersion: ").append(sdkVersion).append("\nappName: ").append(appName).append("\nminSdkVersion: ").append(minSdkVersion).append("\nversionCode: ").append(versionCode).append("\nversionName: ").append(versionName).append("\ntargetSdkVersion: ").append(targetSdkVersion);
        System.out.println(builder.toString());
    }

    public void readPlatform_Properties() {
        try {
            byte[] data = apkFile.getFileData("assets/platform_config.properties");

            File file = createFile(cacheFilePath + "platfrom_config.properties");

            try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file))) {
                writer.write(data, 0, data.length);
            }

            Properties prop = new Properties();
            try (FileInputStream input = new FileInputStream(file)) {
                prop.load(input);
            }
            Enumeration<?> keys = prop.propertyNames();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                System.out.println(key + "--" + prop.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readIcon() {
        try {
            List<IconFace> iconList = apkFile.getAllIcons();
            IconFace iconFace = iconList.get(0);
            String suffix = iconFace.getPath().substring(iconFace.getPath().lastIndexOf("."));

            File iconFile = createFile(cacheFilePath + "app_icon" + suffix);
            System.out.println("icon file name: " + iconFile.getName());

            try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(iconFile))) {
                output.write(iconFace.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readCertificationMd5() {
        try {
            List<ApkSigner> signers = apkFile.getApkSingers();
            System.out.println("v1 certification data");
            if (signers != null) {
                System.out.println(signers.get(0).getCertificateMetas().get(0).getCertMd5());
            } else {
                System.out.println("v2 certification data");
                for (ApkV2Signer v2Signers : apkFile.getApkV2Singers()) {
                    for (CertificateMeta meta : v2Signers.getCertificateMetas()) {
                        System.out.println(meta.getCertMd5());
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    private File createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) System.out.println(file.getParent() + "创建失败");
        }
        if (!file.exists()) {
            if (!file.createNewFile()) System.out.println(file.getName() + "创建失败");
        }
        return file;
    }
}
