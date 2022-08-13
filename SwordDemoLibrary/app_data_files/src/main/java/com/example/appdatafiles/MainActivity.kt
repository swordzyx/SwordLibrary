package com.example.appdatafiles

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.sword.TextUtil
import com.sword.isPermissionGranted
import com.sword.requestSinglePermission
import java.io.*

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (!isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
      requestSinglePermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    queryMediaStore().forEach { image ->
      Log.d("Image", image.toString())
    }

    //writeToPublicDirectory()
  }

  fun onClick(v: View) {
    openGallery()
  }

  private fun queryMediaStore(): List<Image> {
    val imageList = mutableListOf<Image>()

    val projection = arrayOf(
      MediaStore.Images.Media._ID,
      MediaStore.Images.Media.DISPLAY_NAME,
      MediaStore.Images.Media.SIZE
    )
    /*val selection = "${MediaStore.Images.Media.SIZE} < ?"
    val selectionsArgs = arrayOf("40000")*/
    val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"

    val query = contentResolver.query(
      MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
      projection,
      null,
      null,
      sortOrder
    )

    query.use { cursor ->
      if (cursor == null) {
        return imageList
      }

      val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
      val disPlayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
      val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

      while (cursor.moveToNext()) {
        val id = cursor.getLong(idColumn)
        val displayName = cursor.getString(disPlayNameColumn)
        val size = cursor.getInt(sizeColumn)
        imageList += Image(id, displayName, size)
      }
    }

    return imageList
  }

  //需要 WRITE_EXTERNAL_STORAGE 权限
  private fun writeToPublicDirectory() {
    val publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
    val file = File(publicDir, "data_file.txt")

    if (!publicDir.exists()) {
      publicDir.mkdirs()
    }

    if (!file.exists()) {
      file.createNewFile();
    }

    BufferedWriter(OutputStreamWriter(FileOutputStream(file))).apply {
      write("writeToPublicDirectory >>>")
      close()
    }

    BufferedReader(InputStreamReader(FileInputStream(file))).apply {
      var line = readLine()
      while (line != null) {
        Log.d("DataFiles", "read data_file.txt >> $line")
        line = readLine()
      }
      close()
    }
  }

  private fun openGallery() {
    val intent = Intent(Intent.ACTION_PICK).apply {
      setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
      startActivityForResult(this, 1)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 1) {
      val uri = data?.data
      Log.d("DataFiles", "select file uri: ${uri?.path} - ${uri?.authority} - ${uri?.pathSegments} - ${uri?.lastPathSegment}")
      if (uri == null || TextUtils.isEmpty(uri.path)) {
        return
      }
      val file = File(uri.lastPathSegment!!)
      
      FileInputStream(file).apply { 
        Log.d("DataFiles", readBytes().toString())
      }
    }
  }
}

data class Image(val id: Long, val displayName: String, val size: Int)