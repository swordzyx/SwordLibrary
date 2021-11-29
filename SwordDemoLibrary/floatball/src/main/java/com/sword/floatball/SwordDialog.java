package com.sword.floatball;

import android.app.Dialog;
import android.content.Context;

import androidx.appcompat.app.AppCompatDialog;

public class SwordDialog extends AppCompatDialog {
  private SwordDialog(Context context) {
    super(context);
  }
  
  /**
   *  
   */
  public static Dialog createDialog(Context context) {
    return new SwordDialog(context);
  }
  
  private void setLocation() {
    
  }
  
}
