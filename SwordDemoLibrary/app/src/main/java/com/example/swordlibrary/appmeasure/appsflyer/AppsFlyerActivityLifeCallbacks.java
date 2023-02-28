package com.example.swordlibrary.appmeasure.appsflyer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class AppsFlyerActivityLifeCallbacks implements Application.ActivityLifecycleCallbacks {
  public static final AppsFlyerActivityLifeCallbacks instance = new AppsFlyerActivityLifeCallbacks();
  
  @Override
  public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    
  }

  @Override
  public void onActivityStarted(@NonNull Activity activity) {

  }

  @Override
  public void onActivityResumed(@NonNull Activity activity) {

  }

  @Override
  public void onActivityPaused(@NonNull Activity activity) {

  }

  @Override
  public void onActivityStopped(@NonNull Activity activity) {

  }

  @Override
  public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

  }

  @Override
  public void onActivityDestroyed(@NonNull Activity activity) {

  }
  
  interface interface1 {
    void onBecameForeground(Activity activity);
    void interface2Method2(WeakReference<Context> contextWeakReference);
  }
}
