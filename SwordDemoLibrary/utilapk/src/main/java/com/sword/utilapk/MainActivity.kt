package com.sword.utilapk

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.example.utilclass.*
import java.io.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (!isPermissionGranted(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      requestSinglePermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
  }

  fun onClick(view: android.view.View) {

  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
      finish()
    }
  }
}