package sword.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sword.LogUtil

class PermissionMain {
  private val tag = "PermissionMain"
  
  private fun requestPermission(activity: Activity) {
    if (ContextCompat.checkSelfPermission(
        activity,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      LogUtil.debug(tag, "requestPermission")
      ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        0
      )
    }
  }
}