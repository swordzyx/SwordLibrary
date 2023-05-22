package sword.camera

import android.content.Context
import android.content.Intent

class CameraFragment {
  private fun showGallery(context: Context, isCrop: Boolean) {
    val intent: Intent = Intent(context, sword.camera.GalleryActivity::class.java)
    intent.putExtra("crop_photo", isCrop)
    context.startActivity(intent)
  }
}