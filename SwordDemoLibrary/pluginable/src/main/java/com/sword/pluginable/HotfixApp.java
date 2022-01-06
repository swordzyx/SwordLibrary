package com.sword.pluginable;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

public class HotfixApp extends Application {
  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);

    
    //替换 BaseDexClassLoader.pathList.dexElements 为我们自定义的 dexClassLoader.pathList.dexElements
    //loadHotfixApk(getCacheDir() + "/hotfix.apk");
    loadHotfixDex(getCacheDir() + File.separator + "hotfix.dex");
  }

  private void loadHotfixDex(String hotfixDexPath) {
    File dex = new File(hotfixDexPath);
    if (dex.exists()) {
      ClassLoader originClassLoader = getClassLoader();
      ClassLoader hotfixClassLoader = new DexClassLoader(hotfixDexPath, getCacheDir().getPath(), null, null);
      addDexElements(originClassLoader, hotfixClassLoader);
    }
  }

  private void addDexElements(ClassLoader originClassLoader, ClassLoader hotfixClassLoader) {
    //originClassLoader.pathList.dexElements += hotfixClassLoader.pathList.dexElements
    try {
      @SuppressLint("DiscouragedPrivateApi") 
      Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
      pathListField.setAccessible(true);
      Object originPathList = pathListField.get(originClassLoader);
      if (originPathList == null) {
        return;
      }
      Field dexElementsField = originPathList.getClass().getDeclaredField("dexElements");
      dexElementsField.setAccessible(true);
      Object originDexElements = dexElementsField.get(originPathList);
      
      Object hotfixPathList = pathListField.get(hotfixClassLoader);
      Object hotfixDexElements = dexElementsField.get(hotfixPathList);
      
      if (originDexElements == null || hotfixDexElements == null) {
        return;
      }
      int originDexLength = Array.getLength(originDexElements);
      int hotfixDexLength = Array.getLength(hotfixDexElements);
      Object finalDexElements = Array.newInstance(originDexElements.getClass().getComponentType(), originDexLength + hotfixDexLength);
      for (int i=0; i<hotfixDexLength; i++) {
        Array.set(finalDexElements, i, Array.get(hotfixDexElements, i));
      }
      for (int i=0; i<originDexLength; i++) {
        Array.set(finalDexElements, hotfixDexLength + i, Array.get(originDexElements, i));
      }
      
      dexElementsField.set(originPathList, finalDexElements);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private void loadHotfixApk(String hotfixApkPath) {
    File apk = new File(hotfixApkPath);
    
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
