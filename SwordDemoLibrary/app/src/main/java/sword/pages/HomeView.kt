package sword.pages

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.swordlibrary.R

class HomeView(private val context: Context, private val layoutId: Int) {
  @SuppressLint("InflateParams")
  fun view(): View = LayoutInflater.from(context).inflate(R.layout.view_home, null)
  
  //todo: 选取图片
}