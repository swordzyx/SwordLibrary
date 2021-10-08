package com.example.camera

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import com.example.utilclass.PermissionRequestUtil
import kotlinx.android.synthetic.main.take_photo_activity.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TakePhotoActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.take_photo_activity)

        val permissions = PermissionRequestUtil(this)
        permissions.runtimePermission

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    fun takePhoto(view: View) {
        if (view.id == R.id.take_photo) {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            //获取处理此 Intent 的 Activity 组件
            takePictureIntent.resolveActivity(packageManager).also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    //返回 “content://” 格式的 URI。对于 targetSdkVersion 为 24（Android 7.0）及更高的设备，传递 ”file://“ 会导致 FileUriExposedException 异常。使用 FileProvider 存储图片是一种更为通用的方式。 
                    val photoURI =
                        FileProvider.getUriForFile(this, "com.example.android.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    lateinit var currentPhotoPath: String

    /**
     * 在 getExternalFilesDir(Environment.DIRCTORY_PICTURES) 下创建一个 jpg 文件，Android 4.3(API 18) 及以下的系统中，需要声明 WRITE_EXTERNAL_STORAGE 权限
     */
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JEPG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    //usage: createUriForFile("my_images", "default_image.jpg")
    private fun createUriForFile(dirName: String, fileName: String): Uri {
        val imagePath = File(filesDir, dirName)
        val newFile = File(imagePath, fileName);
        return getUriForFile(this, "com.mydomain.fileprovider", newFile)
    }
}