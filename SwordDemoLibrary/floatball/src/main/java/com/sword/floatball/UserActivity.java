package com.sword.floatball;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
  private static final String FEEDBACK_URL = "http://192.168.18.86:8080/feedback";
  private static final String ACCOUNT_URL = "http://192.168.18.86:8080/account";
  private static final String 
  
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);
    
    initView();
  }
  
  private void initView() {
    ImageView imageView = findViewById(R.id.avatar);
    
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.float_menu_change_password) {
      Intent intent = new Intent(this, FLoatMenuActivity.class);
      intent.putExtra("url", )
    }
  }
}
