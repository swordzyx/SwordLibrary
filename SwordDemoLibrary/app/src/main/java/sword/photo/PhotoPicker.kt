package sword.photo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore

/**
 * 图片选择器
 */
class PhotoPicker {
  private val ALBUM_CODE = 1
  
  /**
   * 选取图片，通过 EXTERNAL_CONTENT_URI
   */
  fun pickPhoto(context: Activity) {
    val intent = Intent("android.intent.action.PICK", null)
    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
    context.startActivityForResult(intent, ALBUM_CODE)
  }

  fun showGallery(context: Context, isCrop: Boolean) {
    val intent: Intent = Intent(context, XlcwGalleryActivity::class.java)
    intent.putExtra("crop_photo", isCrop)
    context.startActivity(intent)
  }
}