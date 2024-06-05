package sword

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import sword.devicedetail.getCpuModel
import sword.logger.SwordLog
import sword.pages.ContentPage
import sword.utils.AndroidFileSystem
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
  private val tag = "MainActivity"
  private var contentPage: ContentPage? = null
  private val rootView: View?
    get() {
      return contentPage?.rootView
    }
  private val backPressedListenerList = mutableListOf<BackPressedListener>()

  @SuppressLint("SetTextI18n", "InflateParams")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initWindowSize(this)
    
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
      fullScreenByFlag(window)
    }

    contentPage = ContentPage(this)
    setContentView(rootView)
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      fullScreenByInsetController(window)
    }
    
    SwordLog.debug(tag, getCpuModel())
    AndroidFileSystem.printFileSystemInfo(this)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      getAppSignatureApi28("com.aiwinn.faceattendance")
    }

  }

  @RequiresApi(api = Build.VERSION_CODES.P)
  private fun getAppSignatureApi28(packageName: String) {
    try {
      val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
      if (packageInfo.signingInfo != null) {
        for (signature in packageInfo.signingInfo.apkContentsSigners) {
          val md = MessageDigest.getInstance("SHA")
          md.update(signature.toByteArray())
          val signatureBase64: String = Base64.encodeToString(md.digest(), Base64.DEFAULT)
          SwordLog.debug(tag, "Signature (Base64): $signatureBase64")

          val md5: String = getMD5(signature.toByteArray())
          SwordLog.debug(tag, "md5: $md5")
        }
      }
    } catch (e: PackageManager.NameNotFoundException) {
      e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
    }
  }

  private fun getMD5(data: ByteArray): String {
    try {
      val md = MessageDigest.getInstance("MD5")
      val digest = md.digest(data)
      val sb = StringBuilder()
      for (b in digest) {
        sb.append(String.format("%02X", b))
      }
      return sb.toString()
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
      return "null"
    }
  }
  
  fun addBackListener(listener: BackPressedListener) {
    backPressedListenerList.add(listener)
  } 
  
  
  @Deprecated("Deprecated in Java")
  override fun onBackPressed() {
    SwordLog.debug(tag, "onBackPress")
    
    for (i in backPressedListenerList.size - 1 downTo 0) {
      if (backPressedListenerList[i].onBackPressed()) {
        return
      }
    }
    super.onBackPressed()
  }
  
  fun interface BackPressedListener {
    fun onBackPressed(): Boolean
  }
}