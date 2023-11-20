package sword

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import sword.logger.SwordLog
import sword.pages.HomePage
import sword.photo.XlcwPhotoPicker
import sword.view.*

class MainActivity : AppCompatActivity() {
  private val tag = "MainActivity"
  private lateinit var rootView: View

  @SuppressLint("SetTextI18n", "InflateParams")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initWindowSize(this)


    /*if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      SwordLog.debug(tag, "申请读权限")
      ActivityCompat.requestPermissions(
        this,
        arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        111
      )
    }*/

    if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_MEDIA_IMAGES
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      SwordLog.debug(tag, "申请读权限")
      ActivityCompat.requestPermissions(
        this,
        arrayOf<String>(Manifest.permission.READ_MEDIA_IMAGES),
        111
      )
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
      fullScreenByFlag(window)
    }

    rootView = HomePage(this).rootView
    setContentView(rootView)
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      fullScreenByInsetController(window)
    }
  }
  
  
  @Deprecated("Deprecated in Java")
  override fun onBackPressed() {
    super.onBackPressed()
    SwordLog.debug(tag, "onBackPress")
  }
}