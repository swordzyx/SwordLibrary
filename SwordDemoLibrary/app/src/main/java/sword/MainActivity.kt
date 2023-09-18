package sword

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Trace
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import com.example.swordlibrary.R
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
    
    SwordLog.printStackTrace("打印堆栈")
    
    rootView = findViewById(R.id.rootView)
    contentView = findViewById(R.id.contentView)
    
    initView()
  }
  
  private fun initView() {
    container = findViewById(R.id.contentView)
    
    //自定义 View 触摸反馈：多点触控的原理和写法全解析练习代码
    addMultiTouchView1()
    addMultiTouchView2()
    addMultiTouchView3()
    addMultiTouchView4()

    //自定义 View 触摸反馈：ViewGroup 的触摸反馈，自定义 ViewPager
    addTwoPager()

    //自定义 View 触摸反馈：自定义触摸算法之拖拽 API 详解练习代码
    addDragListenerGridView()
    addDragHelperGridView()
    addDragToCollectLayout()
    addDragUpDownLayout()

    //自定义 View 触摸反馈：嵌套滑动
    addNestScrollViewLayout()

    //自定义 View 触摸反馈：双向滑动的 ScalableImageView 
    addScaleableImageView()

    //约束布局
    addConstraintLayout()

    //MotionLayout 示例
    addMotionLayoutContainer()
  }


  private var dragListenerGridView: DragListenerGridView? = null

  @SuppressLint("InflateParams")
  private fun addDragListenerGridView() {
    if (dragListenerGridView == null) {
      val dragListenerGridView = LayoutInflater.from(this@MainActivity)
        .inflate(R.layout.drag_listener_grid_view, null) as DragListenerGridView
      rootView.addView(
        createButtonToShowViewInContainer("DragListenerGridView", dragListenerGridView),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }

  private var twoPager: TwoPager? = null
  private fun addTwoPager() {
    if (twoPager == null) {
      twoPager = TwoPager(this).apply {
        addView(View(this@MainActivity).apply {
          setBackgroundColor(Color.parseColor("#795548"))
        })
        addView(View(this@MainActivity).apply {
          setBackgroundColor(Color.parseColor("#388E3C"))
        })
      }

      rootView.addView(
        createButtonToShowViewInContainer("TwoPager", twoPager!!),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }

  private var multiTouchView4: MultiTouchView4? = null
  private fun addMultiTouchView4() {
    if (multiTouchView4 == null) {
      multiTouchView4 = MultiTouchView4(this)
      rootView.addView(
        createButtonToShowViewInContainer("MultiTouchView4 各自为战型", multiTouchView4!!),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }

  private var multiTouchView3: MultiTouchView3? = null
  private fun addMultiTouchView3() {
    multiTouchView3 = MultiTouchView3(this)
    rootView.addView(
      createButtonToShowViewInContainer("MultiTouchView3 协作型滑动", multiTouchView3!!),
      LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
    )
  }

  private var multiTouchView2: MultiTouchView2? = null
  private fun addMultiTouchView2() {
    if (multiTouchView2 == null) {
      multiTouchView2 = MultiTouchView2(
        this@MainActivity
      )
      contentView.addView(
        createButtonToShowViewInContainer("MultiTouchView2 接力型滑动", multiTouchView2!!),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }

  private var multiTouchView1: MultiTouchView1? = null
  private fun addMultiTouchView1() {
    if (multiTouchView1 == null) {
      multiTouchView1 = MultiTouchView1(
        this@MainActivity
      )
      rootView.addView(
        createButtonToShowViewInContainer(
          "MultiTouchView1 单点触控",
          multiTouchView1!!
        ), LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }

  private var scaleableImageView: ScaleableImageView? = null
  private fun addScaleableImageView() {
    if (scaleableImageView == null) {
      scaleableImageView = ScaleableImageView(this)
      rootView.addView(
        createButtonToShowViewInContainer("ScaleableImageView", scaleableImageView!!),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }

  private var dragToCollectLayout: DragToCollectLayout? = null

  @SuppressLint("InflateParams")
  private fun addDragToCollectLayout() {
    if (dragToCollectLayout == null) {
      dragToCollectLayout = LayoutInflater.from(this@MainActivity)
        .inflate(R.layout.drag_to_collect_layout, null) as DragToCollectLayout
      rootView.addView(
        createButtonToShowViewInContainer(
          "DragToCollectLayout",
          dragToCollectLayout!!
        ), LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }

  }

  private var dragHelperGridView: DragHelperGridView? = null
  @SuppressLint("InflateParams")
  private fun addDragHelperGridView() {
    if (dragHelperGridView == null) {
      dragHelperGridView = LayoutInflater.from(this@MainActivity)
        .inflate(R.layout.drag_helper_grid_view, null) as DragHelperGridView
      rootView.addView(
        createButtonToShowViewInContainer("DragHelperGridView", dragHelperGridView!!),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }

  private var dragUpDownLayout: DragUpDownLayout? = null
  @SuppressLint("SetTextI18n", "InflateParams")
  private fun addDragUpDownLayout() {
    if (dragUpDownLayout == null) {
      dragUpDownLayout = LayoutInflater.from(this@MainActivity)
        .inflate(R.layout.drag_up_down, null) as DragUpDownLayout
      rootView.addView(
        createButtonToShowViewInContainer(
          "DragUpDownLayout",
          dragUpDownLayout!!
        ), LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }

  private var nestedScrollViewLayout: NestedScrollView? = null
  @SuppressLint("SetTextI18n", "InflateParams")
  private fun addNestScrollViewLayout() {
    if (nestedScrollViewLayout == null) {
      nestedScrollViewLayout = LayoutInflater.from(this@MainActivity)
        .inflate(R.layout.nested_scalable_image_view, null) as NestedScrollView
      rootView.addView(
        createButtonToShowViewInContainer("NestedScrollViewLayout", nestedScrollViewLayout!!),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }
  
  //约束布局示例
  private var constraintLayoutContainer: ConstraintLayoutSampleContainer? = null
  private fun addConstraintLayout() {
    if (constraintLayoutContainer == null) {
      constraintLayoutContainer = ConstraintLayoutSampleContainer(this)
      rootView.addView(
        createButtonToShowViewInContainer(
          "约束布局相关示例", constraintLayoutContainer!!),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }


  //MotionLayout 示例
  private var motionLayoutContainer: MotionLayoutContainer? = null
  private fun addMotionLayoutContainer() {
    if (motionLayoutContainer == null) {
      motionLayoutContainer = MotionLayoutContainer(this)
      rootView.addView(
        createButtonToShowViewInContainer(
          "MotionLayout 相关示例", motionLayoutContainer!!),
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).setVerticalMargin()
      )
    }
  }

  /**
   * 创建一个 Button
   * [buttonName]: 按钮显示的文本
   * [showView]: 点击按钮后要添加到 [container] 中并显示的 view
   */
  private fun createButtonToShowViewInContainer(
    buttonName: String,
    showView: View
  ): AppCompatButton {
    val padding = dp(20)
    return AppCompatButton(this).apply {
      text = buttonName
      isAllCaps = false
      
      setPadding(padding, 0, padding, 0)
      setBackgroundResource(R.drawable.button_background_circle_corner)
      setTextColor(Color.WHITE)
      setOnClickListener {
        container?.visibility = View.VISIBLE
        container?.addView(
          showView,
          LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )

        hideAllView()
        if (showView.visibility != View.VISIBLE) {
          showView.visibility = View.VISIBLE
        }
      }
    }
  }

  private fun hideAllView() {
    container?.children?.forEach { child ->
      if (child.visibility != View.GONE) {
        child.visibility = View.GONE
      }
    }
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
  
  private val defaultMargin = dp(2)
  private fun LayoutParams.setVerticalMargin(margin: Int = defaultMargin): LayoutParams{
    setMargins(0, margin, 0, margin)
    SwordLog.debug(tag, "defaultMargin: $defaultMargin")
    return this
  }
}