package sword.pages

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.swordlibrary.R
import sword.photo.XlcwPhotoPicker

class HomeView(private val context: Activity, private val layoutId: Int) {
  @SuppressLint("InflateParams")
  fun view(): View = xlcwPhotoView()
  private fun xlcwPhotoView(): View = XlcwPhotoPicker.mainView(context)

  private fun mainView(): View = LayoutInflater.from(context).inflate(R.layout.view_photo_picker, null)
  
  //todo: 选取图片
}