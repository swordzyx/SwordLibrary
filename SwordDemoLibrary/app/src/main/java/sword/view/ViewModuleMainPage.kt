package sword.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.NestedScrollView
import com.example.swordlibrary.R
import sword.MainActivity
import sword.dp
import sword.logger.SwordLog
import sword.motionlayout.MotionLayoutContainer
import sword.view.constraint.ConstraintLayoutSampleContainer
import sword.view.dashboard.DashBoardView
import sword.view.pie.PieView

class ViewModuleMainPage(val activity: Activity) {
  private val defaultMargin = 2.dp
  private var currentView: View? = null

  private val mainContainer = FrameLayout(activity).apply {
    layoutParams = ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT)
    setBackgroundColor(Color.DKGRAY)
  }

  val container: ScrollView = ScrollView(activity).apply {
    addView(mainContainer, ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT))
  }

  private val buttonContainer = LinearLayout(activity).apply {
    orientation = LinearLayout.VERTICAL
    layoutParams = ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    )
    setBackgroundColor(Color.GRAY)
  }
  
  init {
    if (activity is MainActivity) {
      activity.addBackListener(MainActivity.BackPressedListener {
        SwordLog.debug(tag, "onBackPress")
        if (currentView != null && mainContainer.indexOfChild(currentView) != -1) {
          mainContainer.removeView(currentView)
          if (buttonContainer.visibility != View.VISIBLE) {
            buttonContainer.visibility = View.VISIBLE
          }
          return@BackPressedListener true
        }
        false
      })
      
    }
    mainContainer.addView(buttonContainer)
    initButtonViews()
  }

  private var dragListenerGridView: DragListenerGridView? = null
  private val tag = "ViewModuleMainPage"

  private fun initButtonViews() {
    addCircleXfermode()
    addPieView()
    addShaderSampleView()
    addColorFilerSampleView()
    addStrokeConfigSampleView()
    addDashboardView()
    addPathEffectSampleView()
    addShadowSampleView()
    addMaskFilterSampleView()
    addFillPathSampleView()
    addTextPathSampleView()

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

  private fun addTextPathSampleView() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("TextPath 效果示例", TextPathSampleView(activity).apply {
        setPadding(defaultMargin * 2, defaultMargin * 2, defaultMargin * 2, defaultMargin * 2)
      }),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  private fun addFillPathSampleView() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("FillPath 效果示例", FillPathSampleView(activity).apply {
        setPadding(defaultMargin * 2, defaultMargin * 2, defaultMargin * 2, defaultMargin * 2)
      }),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  private fun addMaskFilterSampleView() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("MaskFilter 效果示例", MaskFilterSampleView(activity).apply {
        setPadding(defaultMargin * 2, defaultMargin * 2, defaultMargin * 2, defaultMargin * 2)
      }),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  private fun addShadowSampleView() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("ShadowLayer 效果示例", ShadowLayerSampleView(activity).apply {
        setPadding(defaultMargin * 2, defaultMargin * 2, defaultMargin * 2, defaultMargin * 2)
      }),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  private fun addPathEffectSampleView() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("PathEffect 效果示例", PathEffectSampleView(activity).apply {
        setPadding(defaultMargin * 2, defaultMargin * 2, defaultMargin * 2, defaultMargin * 2)
      }),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  private fun addStrokeConfigSampleView() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("线条样式示例", StrokeConfigSampleView(activity).apply {
        setPadding(defaultMargin * 2, defaultMargin * 2, defaultMargin * 2, defaultMargin * 2)
      }),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  private fun addColorFilerSampleView() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("ColorFilter 示例", ColorFilterSampleView(activity).apply {
        setPadding(defaultMargin * 2, defaultMargin * 2, defaultMargin * 2, defaultMargin * 2)
      }),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  private fun addShaderSampleView() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("Shader 着色器示例", ShaderSampleView(activity).apply {
        setPadding(defaultMargin * 2, defaultMargin * 2, defaultMargin * 2, defaultMargin * 2)
      }),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  private fun addCircleXfermode() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("Xfermode 示例", XfermodeViewSampleView(activity)),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }
  
  private fun addPieView() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("饼图", PieView(activity).apply { 
        setPadding(defaultMargin * 2, defaultMargin * 2, defaultMargin * 2, defaultMargin * 2)
      }),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  //仪表盘
  private fun addDashboardView() {
    buttonContainer.addView(
      createButtonToShowViewInContainer("仪表盘", DashBoardView(activity)),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  @SuppressLint("InflateParams")
  private fun addDragListenerGridView() {
    if (dragListenerGridView == null) {
      val dragListenerGridView = LayoutInflater.from(activity)
        .inflate(R.layout.drag_listener_grid_view, null) as DragListenerGridView
      buttonContainer.addView(
        createButtonToShowViewInContainer("DragListenerGridView", dragListenerGridView),
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }
  }

  private var twoPager: TwoPager? = null
  private fun addTwoPager() {
    if (twoPager == null) {
      twoPager = TwoPager(activity).apply {
        addView(View(activity).apply {
          setBackgroundColor(Color.parseColor("#795548"))
        })
        addView(View(activity).apply {
          setBackgroundColor(Color.parseColor("#388E3C"))
        })
      }

      buttonContainer.addView(
        createButtonToShowViewInContainer("TwoPager", twoPager!!),
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }
  }

  private var multiTouchView4: MultiTouchView4? = null
  private fun addMultiTouchView4() {
    if (multiTouchView4 == null) {
      multiTouchView4 = MultiTouchView4(activity)
      buttonContainer.addView(
        createButtonToShowViewInContainer("MultiTouchView4 各自为战型", multiTouchView4!!),
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }
  }

  private var multiTouchView3: MultiTouchView3? = null
  private fun addMultiTouchView3() {
    multiTouchView3 = MultiTouchView3(activity)
    buttonContainer.addView(
      createButtonToShowViewInContainer("MultiTouchView3 协作型滑动", multiTouchView3!!),
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).setVerticalMargin()
    )
  }

  private var multiTouchView2: MultiTouchView2? = null
  private fun addMultiTouchView2() {
    if (multiTouchView2 == null) {
      multiTouchView2 = MultiTouchView2(activity)
      buttonContainer.addView(
        createButtonToShowViewInContainer("MultiTouchView2 接力型滑动", multiTouchView2!!),
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }
  }

  private var multiTouchView1: MultiTouchView1? = null
  private fun addMultiTouchView1() {
    if (multiTouchView1 == null) {
      multiTouchView1 = MultiTouchView1(
        activity
      )
      buttonContainer.addView(
        createButtonToShowViewInContainer(
          "MultiTouchView1 单点触控",
          multiTouchView1!!
        ), LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }
  }

  private var scaleableImageView: ScaleableImageView? = null
  private fun addScaleableImageView() {
    if (scaleableImageView == null) {
      scaleableImageView = ScaleableImageView(activity)
      buttonContainer.addView(
        createButtonToShowViewInContainer("ScaleableImageView", scaleableImageView!!),
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }
  }

  private var dragToCollectLayout: DragToCollectLayout? = null

  @SuppressLint("InflateParams")
  private fun addDragToCollectLayout() {
    if (dragToCollectLayout == null) {
      dragToCollectLayout = LayoutInflater.from(activity)
        .inflate(R.layout.drag_to_collect_layout, null) as DragToCollectLayout
      buttonContainer.addView(
        createButtonToShowViewInContainer(
          "DragToCollectLayout",
          dragToCollectLayout!!
        ), LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }

  }

  private var dragHelperGridView: DragHelperGridView? = null

  @SuppressLint("InflateParams")
  private fun addDragHelperGridView() {
    if (dragHelperGridView == null) {
      dragHelperGridView = LayoutInflater.from(activity)
        .inflate(R.layout.drag_helper_grid_view, null) as DragHelperGridView
      buttonContainer.addView(
        createButtonToShowViewInContainer("DragHelperGridView", dragHelperGridView!!),
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }
  }

  private var dragUpDownLayout: DragUpDownLayout? = null

  @SuppressLint("SetTextI18n", "InflateParams")
  private fun addDragUpDownLayout() {
    if (dragUpDownLayout == null) {
      dragUpDownLayout = LayoutInflater.from(activity)
        .inflate(R.layout.drag_up_down, null) as DragUpDownLayout
      buttonContainer.addView(
        createButtonToShowViewInContainer("DragUpDownLayout", dragUpDownLayout!!), 
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }
  }

  private var nestedScrollViewLayout: NestedScrollView? = null

  @SuppressLint("SetTextI18n", "InflateParams")
  private fun addNestScrollViewLayout() {
    if (nestedScrollViewLayout == null) {
      nestedScrollViewLayout = LayoutInflater.from(activity)
        .inflate(R.layout.nested_scalable_image_view, null) as NestedScrollView
      buttonContainer.addView(
        createButtonToShowViewInContainer(
          "NestedScrollViewLayout",
          nestedScrollViewLayout!!),
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }
  }

  //约束布局示例
  private var constraintLayoutContainer: ConstraintLayoutSampleContainer? = null
  private fun addConstraintLayout() {
    if (constraintLayoutContainer == null) {
      constraintLayoutContainer = ConstraintLayoutSampleContainer(activity)
      buttonContainer.addView(
        createButtonToShowViewInContainer(
          "约束布局相关示例", constraintLayoutContainer!!
        ),
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
      )
    }
  }


  //MotionLayout 示例
  private var motionLayoutContainer: MotionLayoutContainer? = null
  private fun addMotionLayoutContainer() {
    if (motionLayoutContainer == null) {
      motionLayoutContainer = MotionLayoutContainer(activity)
      buttonContainer.addView(
        createButtonToShowViewInContainer(
          "MotionLayout 相关示例", motionLayoutContainer!!
        ),
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).setVerticalMargin()
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
    showView: View): AppCompatButton {
    val padding = 20.dp
    return AppCompatButton(activity).apply {
      text = buttonName
      isAllCaps = false

      setPadding(padding, 0, padding, 0)
      setBackgroundResource(R.drawable.background_circle_corner_blue)
      setTextColor(Color.WHITE)
      setOnClickListener {
        if (buttonContainer.visibility != View.GONE) {
          buttonContainer.visibility = View.GONE
        }
        mainContainer.addView(
          showView,
          LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
          )
        )
        currentView = showView
      }
    }
  }

  
  private fun LinearLayout.LayoutParams.setVerticalMargin(margin: Int = defaultMargin): LinearLayout.LayoutParams {
    setMargins(0, margin, 0, margin)
    return this
  }
}