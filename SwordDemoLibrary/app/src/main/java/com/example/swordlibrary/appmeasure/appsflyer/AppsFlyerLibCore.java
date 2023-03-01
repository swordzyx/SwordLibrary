package com.example.swordlibrary.appmeasure.appsflyer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;
import java.util.Map;

public class AppsFlyerLibCore {
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
          long time = System.currentTimeMillis();
          //
        }

        @Override
        public void interface2Method2(WeakReference<Context> contextWeakReference) {

        }
      };
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
