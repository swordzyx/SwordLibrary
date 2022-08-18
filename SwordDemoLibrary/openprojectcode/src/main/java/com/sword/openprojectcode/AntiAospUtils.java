package com.sword.openprojectcode;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.sword.LogUtil;

/**
 * 参考：https://www.jianshu.com/p/e3a9c4f752c5
 */
public class AntiAospUtils {
  public static final String tag = "AntiAospUtils";
  
  private static final String NATIVE_LIB_X86 = "x86";

  /**
   * 扫描 CPU 架构信息
   */
  private void checkDeviceForumX86() {
    
  }
  
  private String getNativeLibDirName(Context context, String packageName) {
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
      String nativeLibraryDir = packageInfo.applicationInfo.nativeLibraryDir;
      LogUtil.debug(tag, "getNativeLibDirName, packageName: " + packageName + ", nativeLibraryDirName: " + nativeLibraryDir);
      return nativeLibraryDir;
    } catch (PackageManager.NameNotFoundException exception) {
      exception.printStackTrace();
    }
    return "";
  }
  
  
}
