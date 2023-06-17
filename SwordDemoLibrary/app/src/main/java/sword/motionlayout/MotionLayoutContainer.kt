package sword.motionlayout

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.swordlibrary.R
import sword.utils.createButtonToShowViewInContainer
import sword.view.VerticalLinearContainer

class MotionLayoutContainer(context: Context, attrs: AttributeSet? = null) :
  VerticalLinearContainer(context, attrs) {

  
  private var showDebug = false
  
  init {
    addHenCoderMotionSampleButton()
    addYouTubeMotionSampleButton()
    addYouTubeFilmLikeMotionSampleButton()
  }

  private var hencoderMotionLayout: MotionLayout? = null
  @SuppressLint("InflateParams")
  private fun addHenCoderMotionSampleButton() {
    if (hencoderMotionLayout == null) {
      hencoderMotionLayout =
        LayoutInflater.from(context).inflate(R.layout.motion_layout_hen_coder, null) as MotionLayout
      hencoderMotionLayout!!.findViewById<Button>(R.id.showDebug).setOnClickListener {
        showDebug = if (showDebug) {
          hencoderMotionLayout!!.setDebugMode(MotionLayout.DEBUG_SHOW_NONE)
          false
        } else {
          hencoderMotionLayout!!.setDebugMode(MotionLayout.DEBUG_SHOW_PATH)
          true
        }
      }
      addView(
        createButtonToShowViewInContainer(
          "MotionLayout Sample 1",
          hencoderMotionLayout!!, 
          container),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }

  }

  private var youtubeMotionLayout: MotionLayout? = null
  @SuppressLint("InflateParams")
  private fun addYouTubeMotionSampleButton() {
    if (youtubeMotionLayout == null) {
      youtubeMotionLayout =
        LayoutInflater.from(context).inflate(R.layout.motion_layout_youtube, null) as MotionLayout
      youtubeMotionLayout!!.setDebugMode(MotionLayout.DEBUG_SHOW_PATH)
      addView(
        createButtonToShowViewInContainer(
          "MotionLayout Youtube",
          youtubeMotionLayout!!,
          container),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }

  }


  private var youtubeFilmLikeMotionLayout: MotionLayout? = null
  @SuppressLint("InflateParams")
  private fun addYouTubeFilmLikeMotionSampleButton() {
    if (youtubeFilmLikeMotionLayout == null) {
      youtubeFilmLikeMotionLayout =
        LayoutInflater.from(context).inflate(R.layout.motion_youtube_film_like, null) as MotionLayout
      youtubeFilmLikeMotionLayout!!.setDebugMode(MotionLayout.DEBUG_SHOW_PATH)
      addView(
        createButtonToShowViewInContainer(
          "MotionLayout Youtube",
          youtubeFilmLikeMotionLayout!!,
          container),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }

  }

}