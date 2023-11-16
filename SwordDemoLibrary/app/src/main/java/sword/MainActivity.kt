package sword

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import com.example.swordlibrary.R
import sword.logger.SwordLog
import sword.motionlayout.MotionLayoutContainer
import sword.view.*
import sword.view.constraint.ConstraintLayoutSampleContainer

class MainActivity : AppCompatActivity() {
  private val tag = "MainActivity"
  private var container: FrameLayout? = null
  private lateinit var rootView: LinearLayout
  private lateinit var contentView: FrameLayout

  @SuppressLint("SetTextI18n", "InflateParams")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initWindowSize(this)

    setContentView(R.layout.activity_main)
    
    rootView = findViewById(R.id.rootView)
    contentView = findViewById(R.id.contentView)
  }
  

  override fun onBackPressed() {
    container?.children?.forEach { child ->
      if (child is INavigationLayout && child.onBackPressed()) {
        return
      }
    }
    
    if (container?.visibility != View.GONE) {
      container?.visibility = View.GONE
    } else {
      super.onBackPressed()
    }
  }
}