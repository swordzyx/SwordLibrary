package com.sword.script;

public class ApkInfo {
    private String packageName;
    private String versionCode;
    private String code;
    private String IconFile;
    private String apkFileName;
    private String appName;
    private String signMD5;
    private String apkMD5;
    private String size;

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIconFile(String iconFile) {
        IconFile = iconFile;
    }

    public void setApkFileName(String apkFileName) {
        this.apkFileName = apkFileName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setSignMD5(String signMD5) {
        this.signMD5 = signMD5;
    }

    public void setApkMD5(String apkMD5) {
        this.apkMD5 = apkMD5;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
