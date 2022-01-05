package com.sword.pluginable;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.io.File;
import java.lang.reflect.Field;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

public class HotfixApp extends Application {
  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);

    File apk = new File(getCacheDir() + "/hotfix.apk");
    //替换 BaseDexClassLoader.pathList.dexElements 为我们自定义的 dexClassLoader.pathList.dexElements
    if (apk.exists()) {
      ClassLoader originClassLoader = getClassLoader();
      ClassLoader hotfixClassloader = new DexClassLoader(apk.getPath(), getCacheDir().getPath(), null, null);
      replaceDexElements(originClassLoader, hotfixClassloader);
    }
  }

  /**
   * 替换所有的类
   * 
   * 执行 originClassLoader.pathList.dexElements = hotfixClassLoader.pathList.dexElements
   */
  private void replaceDexElements(ClassLoader originClassLoader, ClassLoader hotfixClassLoader) {
    try {
      @SuppressLint("DiscouragedPrivateApi")
      Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
      pathListField.setAccessible(true);
      //获取 originClassLoader.pathList
      Object pathList = pathListField.get(originClassLoader);
      Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
      dexElementsField.setAccessible(true);

      //获取 hotfixClassLoader.pathList
      Object hotfixPathList = pathListField.get(hotfixClassLoader);
      //获取 hotfixClassLoader.pathList.dexElements
      Object hotfixDexElements = dexElementsField.get(hotfixPathList);

      dexElementsField.set(pathList, hotfixDexElements);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
