package com.sword;

import android.app.Application;
import android.content.Context;

public class SwordApplication extends Application {
  private static Context applicationContext;
  
  @Override
  public void onCreate() {
    super.onCreate();
    
    applicationContext = getApplicationContext();
  }
  
  public static Context getGlobalContext() {
    return applicationContext;
  }
}
