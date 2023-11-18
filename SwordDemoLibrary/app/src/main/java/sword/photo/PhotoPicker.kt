package sword.photo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import sword.logger.SwordLog

/**
 * 图片选择器
 */
object PhotoPicker {
  private val tag = "PhotoPciker"
  private val ALBUM_CODE = 1
  val SUCCESS = 1
  val FAILED = 0
  
  fun photoPickerView() {
    
  }

  /**
   * 选取单个图片或视频
   */
  fun pickSingleImageAndVideo(activity: AppCompatActivity, callback: Callback<Uri?>) {
    selectSingleMedia(
      activity,
      PickVisualMedia.ImageAndVideo,
      callback
    )
  }

  /**
   * 选取多个图片或视频
   */
  fun pickMultiImageAndVideo(activity: AppCompatActivity, selectCount: Int, callback: Callback<List<Uri>?>) {
    selectMultiMedia(
      activity,
      PickVisualMedia.ImageAndVideo,
      selectCount,
      callback
    )
  }

  /**
   * 选取单个图片
   */
  fun pickSingleImage(activity: AppCompatActivity, callback: Callback<Uri?>) {
    selectSingleMedia(activity, PickVisualMedia.ImageOnly, callback)
  }

  /**
   * 选取多张图片
   */
  fun pickMultiImage(activity: AppCompatActivity, selectCount: Int, callback: Callback<List<Uri>?>) {
    selectMultiMedia(activity, PickVisualMedia.ImageOnly, selectCount, callback)
  }
  

  /**
   * 选取单个视频
   */
  fun pickSingleVideo(activity: AppCompatActivity, callback: Callback<Uri?>) {
    selectSingleMedia(activity, PickVisualMedia.VideoOnly, callback)
  }

  /**
   * 选取多个视频
   */
  fun pickMultiVideo(activity: AppCompatActivity, selectCount: Int, callback: Callback<List<Uri>?>) {
    selectMultiMedia(activity, PickVisualMedia.VideoOnly, selectCount, callback)
  }

  /**
   * 选取单个指定类型的媒体文件
   */
  fun pickSingleMimeTypeFile(
    activity: AppCompatActivity,
    mimeType: String,
    callback: Callback<Uri?>
  ) {
    val mediaMimeType = PickVisualMedia.SingleMimeType(mimeType)
    selectSingleMedia(activity, mediaMimeType, callback)
  }


  /**
   * 选取多个指定类型的媒体文件
   */
  fun pickMultiMimeTypeFile(
    activity: AppCompatActivity,
    mimeType: String,
    selectCount: Int,
    callback: Callback<List<Uri>?>
  ) {
    val mediaMimeType = PickVisualMedia.SingleMimeType(mimeType)
    selectMultiMedia(activity, mediaMimeType, selectCount, callback)
  }


  /**
   * 选取单个媒体文件（音频、视频、图片）
   */
  private fun selectSingleMedia(
    activity: AppCompatActivity,
    mediaType: PickVisualMedia.VisualMediaType,
    callback: Callback<Uri?>
  ) {
    val pickMedia =
      activity.registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
          SwordLog.debug(tag, "选择图片：$uri")
          callback.onResult(uri, SUCCESS, "")
        } else {
          SwordLog.debug(tag, "未选择文件")
          callback.onResult(null, FAILED, "未选择文件")
        }
      }

    pickMedia.launch(PickVisualMediaRequest(mediaType))
  }


  /**
   * 选取多张图片，默认可选取无上限的图片
   */
  private fun selectMultiMedia(
    activity: AppCompatActivity,
    mediaType: PickVisualMedia.VisualMediaType,
    selecMaxCount: Int = Int.MAX_VALUE,
    callback: Callback<List<Uri>?>
  ) {
    val pickMultiMediaLauncher =
      activity.registerForActivityResult(PickMultipleVisualMedia(selecMaxCount)) { uris ->
        if (uris.isNotEmpty()) {
          callback.onResult(uris, SUCCESS, "")
        } else {
          callback.onResult(null, FAILED, "未选取图片")
        }
      }
    pickMultiMediaLauncher.launch(PickVisualMediaRequest(mediaType))
  }

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

  interface Callback<R> {
    fun onResult(result: R, errorCode: Int, msg: String)
  }
}