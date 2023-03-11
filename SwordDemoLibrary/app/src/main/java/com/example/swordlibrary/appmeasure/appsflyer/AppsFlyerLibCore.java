package com.example.swordlibrary.appmeasure.appsflyer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sword.LogUtil;
import com.sword.ThreadExecutor;

import java.lang.ref.WeakReference;
import java.util.Map;

public class AppsFlyerLibCore {
  private static final String tag = "AppsFlyerLibCore";
  private Application application;
  private AppsFlyerActivityLifeCallbacks.interface1 interface1;
  
  public long time;
  public long time2;
  
  public void startTracking(Application application) {
    this.application = application;
    
    if (interface1 == null) {
      interface1 = new AppsFlyerActivityLifeCallbacks.interface1() {
        @Override
        public void onBecameForeground(Activity activity) {
          time = System.currentTimeMillis();
          //获取 Activity 启动来源相关信息
          Intent intent = activity.getIntent();
          String referrerString = activity.getReferrer().toString();
          LogUtil.debug(tag, "referrer: " + referrerString);
          time2 = System.currentTimeMillis();

          ThreadExecutor.getScheduledThreadExecutor();
        }

        @Override
        public void interface2Method2(WeakReference<Context> contextWeakReference) {

        }
      };
    }
    application.registerActivityLifecycleCallbacks(AppsFlyerActivityLifeCallbacks.instance);
  }
  
  private class Runnable1 implements Runnable {
    @Override
    public void run() {
      
    }
  }

  /*public void trackAppLaunch(Context var1, String var2) {
    if (ˊ(var1)) {
      if (this.ᐝॱ == null) {
        this.ᐝॱ = new j();
        this.ᐝॱ.ˏ(var1, new m() {
          public final void ˏ(Map<String, Object> var1) {
            AppsFlyerLibCore.this.ॱˎ = var1;
            AppsFlyerLibCore.ˋ(AppsFlyerLibCore.this, var1);
          }
        });
      } else {
        AFLogger.afWarnLog("AFInstallReferrer instance already created");
      }
    }

    this.ॱ(var1, var2, "", (Intent)null);
  }*/

}
