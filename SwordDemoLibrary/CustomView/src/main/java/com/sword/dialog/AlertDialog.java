package com.sword.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

public class AlertDialog extends Dialog {
  
  
  public AlertDialog(@NonNull Context context) {
    super(context);
  }

  public AlertDialog(@NonNull Context context, int themeResId) {
    super(context, themeResId);
  }
  
  public static class Builder {
    
  }
}
