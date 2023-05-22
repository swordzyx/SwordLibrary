package sword

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import com.example.swordlibrary.R
import com.sword.LogUtil
import com.sword.initWindowSize
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import sword.view.*

class MainActivity2: AppCompatActivity() {
  private val tag = "MainActivity2"
  private var multiTouchView1: MultiTouchView1? = null
  private var multiTouchView2: MultiTouchView2? = null
  private var multiTouchView3: MultiTouchView3? = null
  private var multiTouchView4: MultiTouchView4? = null
  private var dragListenerGridView: DragListenerGridView? = null
  
  private var twoPager: TwoPager? = null
  
  private var container: FrameLayout? = null

  @SuppressLint("SetTextI18n", "InflateParams")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)
    val rootView = findViewById<LinearLayout>(R.id.rootView)
    container = findViewById(R.id.contentView)

    //扔物线课程触摸反馈练习代码
    addMultiTouchView1()
    val multiTouchView2 = MultiTouchView2(
      this@MainActivity2)
    contentView.addView(multiTouchView2,
      FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    addMultiTouchView2()

    rootView.addView(Button(this).apply {
      text = "MultiTouchView3 协作型滑动"
      setOnClickListener {
        contentView.visibility = View.VISIBLE
        
        if (multiTouchView3 == null) {
          multiTouchView3 = MultiTouchView3(
            this@MainActivity2)
          contentView.addView(multiTouchView3,
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          )
        }

        hideAllView()
        if (multiTouchView3?.visibility != View.VISIBLE) {
          multiTouchView3?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    
    rootView.addView(Button(this).apply {
      text = "MultiTouchView3 各自为战型"
      setOnClickListener {
        contentView.visibility = View.VISIBLE

        if (multiTouchView4 == null) {
          multiTouchView4 = MultiTouchView4(
            this@MainActivity2)
          contentView.addView(multiTouchView4,
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          )
        }

        hideAllView()
        if (multiTouchView4?.visibility != View.VISIBLE) {
          multiTouchView4?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

    rootView.addView(Button(this).apply {
      text = "TwoPager"
      setOnClickListener {
        contentView.visibility = View.VISIBLE

        if (twoPager == null) {
          twoPager = TwoPager(
            this@MainActivity2)
          
          twoPager?.addView(View(context).apply { 
            setBackgroundColor(Color.parseColor("#795548"))
          })
          twoPager?.addView(View(context).apply { 
            setBackgroundColor(Color.parseColor("#388E3C"))
          })
          contentView.addView(twoPager,
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          )
        }

        hideAllView()
        if (twoPager?.visibility != View.VISIBLE) {
          twoPager?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

    rootView.addView(Button(this).apply {
      text = "DragListenerGridView"
      setOnClickListener {
        contentView.visibility = View.VISIBLE

        if (dragListenerGridView == null) {
          dragListenerGridView = LayoutInflater.from(this@MainActivity2).inflate(R.layout.drag_listener_grid_view, null) as DragListenerGridView
          contentView.addView(
            dragListenerGridView, 
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }

        LogUtil.debug("DragListenerGridView", "添加 DragListenerGridView")
        hideAllView()
        if (dragListenerGridView?.visibility != View.VISIBLE) {
          dragListenerGridView?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

    addDragHelperGridView()

    addDragToCollectLayout()

    addDragUpDownLayout()
    
    addNestScrollViewLayout()
    
    addScaleableImageView()
    
    initWindowSize(this)
  }

  @SuppressLint("SetTextI18n")
  private fun addMultiTouchView1() {
    rootView.addView(Button(this).apply {
      text = "MultiTouchView1 单点触控"
      isAllCaps = false
      setOnClickListener {
        contentView.visibility = View.VISIBLE
        if (multiTouchView1 == null) {
          multiTouchView1 = MultiTouchView1(
            this@MainActivity2)
          contentView.addView(multiTouchView1,
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          )
        }

        hideAllView()
        if (multiTouchView1?.visibility != View.VISIBLE) {
          multiTouchView1?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
  }

  @SuppressLint("SetTextI18n")
  private fun addScaleableImageView() {
    rootView.addView(
      createButtonToShowViewInContainer("ScaleableImageView", ScaleableImageView(this)),
      LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
  }

  /**
   * 创建一个 Button
   * [buttonName]: 按钮显示的文本
   * [showView]: 点击按钮后要添加到 [container] 中并显示的 view
   */
  private fun createButtonToShowViewInContainer(buttonName: String, showView: View): AppCompatButton {
    return AppCompatButton(this).apply {
      text = buttonName
      isAllCaps = false
      setOnClickListener {
        container?.visibility = View.VISIBLE
        container?.addView(
          showView,
          LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        hideAllView()
        if (showView.visibility != View.VISIBLE) {
          showView.visibility = View.VISIBLE
        }
      }
    }
  }

  private var dragToCollectLayout: DragToCollectLayout? = null
  @SuppressLint("SetTextI18n")
  private fun addDragToCollectLayout() {
    rootView.addView(Button(this).apply {
      text = "DragToCollectLayout"
      isAllCaps = false
      setOnClickListener {
        container?.visibility = View.VISIBLE

        if (dragToCollectLayout == null) {
          dragToCollectLayout = LayoutInflater.from(this@MainActivity2).inflate(R.layout.drag_to_collect_layout, null) as DragToCollectLayout
          container?.addView(
            dragToCollectLayout,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }

        LogUtil.debug("DragHelperGridView", "添加 DragHelperGridView")
        hideAllView()
        if (dragToCollectLayout?.visibility != View.VISIBLE) {
          dragToCollectLayout?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
  }

  private var dragHelperGridView: DragHelperGridView? = null
  @SuppressLint("SetTextI18n", "InflateParams")
  private fun addDragHelperGridView() {
    rootView.addView(Button(this).apply {
      text = "DragHelperGridView"
      isAllCaps = false
      setOnClickListener {
        container?.visibility = View.VISIBLE

        if (dragHelperGridView == null) {
          dragHelperGridView = LayoutInflater.from(this@MainActivity2).inflate(R.layout.drag_helper_grid_view, null) as DragHelperGridView
          container?.addView(
            dragHelperGridView,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }

        LogUtil.debug("DragHelperGridView", "添加 DragHelperGridView")
        hideAllView()
        if (dragHelperGridView?.visibility != View.VISIBLE) {
          dragHelperGridView?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
  }

  private var dragUpDownLayout: DragUpDownLayout? = null
  @SuppressLint("SetTextI18n", "InflateParams")
  private fun addDragUpDownLayout() {
    rootView.addView(Button(this).apply {
      text = "DragUpDownLayout"
      isAllCaps = false
      setOnClickListener {
        container?.visibility = View.VISIBLE

        if (dragUpDownLayout == null) {
          dragUpDownLayout = LayoutInflater.from(this@MainActivity2).inflate(R.layout.drag_up_down, null) as DragUpDownLayout
          container?.addView(
            dragUpDownLayout,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }

        LogUtil.debug("DragHelperGridView", "添加 DragHelperGridView")
        hideAllView()
        if (dragUpDownLayout?.visibility != View.VISIBLE) {
          dragUpDownLayout?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
  }
  
  private var nestedScrollViewLayout: NestedScrollView? = null
  @SuppressLint("SetTextI18n", "InflateParams")
  private fun addNestScrollViewLayout() {
    rootView.addView(Button(this).apply {
      text = "NestScrollViewLayout"
      isAllCaps = false
      setOnClickListener {
        container?.visibility = View.VISIBLE

        if (nestedScrollViewLayout == null) {
          nestedScrollViewLayout = LayoutInflater.from(this@MainActivity2).inflate(R.layout.nested_scalable_image_view, null) as NestedScrollView
          container?.addView(
            nestedScrollViewLayout,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }

        LogUtil.debug(this@MainActivity2.tag, "添加 NestScrollViewLayout")
        hideAllView()
        if (nestedScrollViewLayout?.visibility != View.VISIBLE) {
          nestedScrollViewLayout?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
  }

  private fun hideAllView() {
    container?.children?.forEach { child ->
      if (child.visibility != View.GONE) {
        child.visibility = View.GONE
      }
    }
  }

  override fun onBackPressed() {
    if (container?.visibility != View.GONE) {
      container?.visibility = View.GONE
    } else {
      super.onBackPressed()
    }
  }
}