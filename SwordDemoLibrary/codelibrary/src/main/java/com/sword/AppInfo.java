package com.sword;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppInfo {
  //应用版本号
  private String appVersion;
  //应用名称
  private final String appName;
  //应用包名
  private final String packageName;

  public AppInfo(Context context) {
    packageName = context.getPackageName();
    appName = context.getApplicationInfo().name;
    
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
      appVersion = packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      appVersion = "";
      e.printStackTrace();
    }
  }
}
