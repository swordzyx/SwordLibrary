package sword.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import com.example.swordlibrary.R
import sword.dp
import sword.logger.SwordLog
import sword.motionlayout.MotionLayoutContainer
import sword.view.constraint.ConstraintLayoutSampleContainer

class ViewModuleMainPage {

    private var dragListenerGridView: DragListenerGridView? = null
    private val tag = "ViewModuleMainPage"

    private fun initView(container: ViewGroup) {
        //自定义 View 触摸反馈：多点触控的原理和写法全解析练习代码
        addMultiTouchView1(container)
        addMultiTouchView2(container)
        addMultiTouchView3(container)
        addMultiTouchView4(container)

        //自定义 View 触摸反馈：ViewGroup 的触摸反馈，自定义 ViewPager
        addTwoPager(container)

        //自定义 View 触摸反馈：自定义触摸算法之拖拽 API 详解练习代码
        addDragListenerGridView(container)
        addDragHelperGridView(container)
        addDragToCollectLayout(container)
        addDragUpDownLayout(container)

        //自定义 View 触摸反馈：嵌套滑动
        addNestScrollViewLayout(container)

        //自定义 View 触摸反馈：双向滑动的 ScalableImageView
        addScaleableImageView(container)

        //约束布局
        addConstraintLayout(container)

        //MotionLayout 示例
        addMotionLayoutContainer(container)
    }

    @SuppressLint("InflateParams")
    private fun addDragListenerGridView(parent: ViewGroup) {
        if (dragListenerGridView == null) {
            val dragListenerGridView = LayoutInflater.from(parent.context)
                .inflate(R.layout.drag_listener_grid_view, null) as DragListenerGridView
            parent.addView(
                createButtonToShowViewInContainer("DragListenerGridView", dragListenerGridView, parent),
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }
    }

    private var twoPager: TwoPager? = null
    private fun addTwoPager(parent: ViewGroup) {
        if (twoPager == null) {
            twoPager = TwoPager(parent.context).apply {
                addView(View(parent.context).apply {
                    setBackgroundColor(Color.parseColor("#795548"))
                })
                addView(View(parent.context).apply {
                    setBackgroundColor(Color.parseColor("#388E3C"))
                })
            }

            parent.addView(
                createButtonToShowViewInContainer("TwoPager", twoPager!!, parent),
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }
    }

    private var multiTouchView4: MultiTouchView4? = null
    private fun addMultiTouchView4(parent: ViewGroup) {
        if (multiTouchView4 == null) {
            multiTouchView4 = MultiTouchView4(parent.context)
            parent.addView(
                createButtonToShowViewInContainer("MultiTouchView4 各自为战型", multiTouchView4!!, parent),
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }
    }

    private var multiTouchView3: MultiTouchView3? = null
    private fun addMultiTouchView3(parent: ViewGroup) {
        multiTouchView3 = MultiTouchView3(parent.context)
        parent.addView(
            createButtonToShowViewInContainer("MultiTouchView3 协作型滑动", multiTouchView3!!, parent),
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).setVerticalMargin()
        )
    }

    private var multiTouchView2: MultiTouchView2? = null
    private fun addMultiTouchView2(parent: ViewGroup) {
        if (multiTouchView2 == null) {
            multiTouchView2 = MultiTouchView2(parent.context)
            parent.addView(
                createButtonToShowViewInContainer("MultiTouchView2 接力型滑动", multiTouchView2!!, parent),
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }
    }

    private var multiTouchView1: MultiTouchView1? = null
    private fun addMultiTouchView1(parent: ViewGroup) {
        if (multiTouchView1 == null) {
            multiTouchView1 = MultiTouchView1(
                parent.context
            )
            parent.addView(
                createButtonToShowViewInContainer(
                    "MultiTouchView1 单点触控",
                    multiTouchView1!!,
                    parent
                ), LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }
    }

    private var scaleableImageView: ScaleableImageView? = null
    private fun addScaleableImageView(parent: ViewGroup) {
        if (scaleableImageView == null) {
            scaleableImageView = ScaleableImageView(parent.context)
            parent.addView(
                createButtonToShowViewInContainer("ScaleableImageView", scaleableImageView!!, parent),
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }
    }

    private var dragToCollectLayout: DragToCollectLayout? = null

    @SuppressLint("InflateParams")
    private fun addDragToCollectLayout(parent: ViewGroup) {
        if (dragToCollectLayout == null) {
            dragToCollectLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.drag_to_collect_layout, null) as DragToCollectLayout
            parent.addView(
                createButtonToShowViewInContainer(
                    "DragToCollectLayout",
                    dragToCollectLayout!!,
                    parent
                ), LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }

    }

    private var dragHelperGridView: DragHelperGridView? = null
    @SuppressLint("InflateParams")
    private fun addDragHelperGridView(parent: ViewGroup) {
        if (dragHelperGridView == null) {
            dragHelperGridView = LayoutInflater.from(parent.context)
                .inflate(R.layout.drag_helper_grid_view, null) as DragHelperGridView
            parent.addView(
                createButtonToShowViewInContainer("DragHelperGridView", dragHelperGridView!!, parent),
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }
    }

    private var dragUpDownLayout: DragUpDownLayout? = null
    @SuppressLint("SetTextI18n", "InflateParams")
    private fun addDragUpDownLayout(parent: ViewGroup) {
        if (dragUpDownLayout == null) {
            dragUpDownLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.drag_up_down, null) as DragUpDownLayout
            parent.addView(
                createButtonToShowViewInContainer(
                    "DragUpDownLayout",
                    dragUpDownLayout!!,
                    parent
                ), LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }
    }

    private var nestedScrollViewLayout: NestedScrollView? = null
    @SuppressLint("SetTextI18n", "InflateParams")
    private fun addNestScrollViewLayout(parent: ViewGroup) {
        if (nestedScrollViewLayout == null) {
            nestedScrollViewLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.nested_scalable_image_view, null) as NestedScrollView
            parent.addView(
                createButtonToShowViewInContainer("NestedScrollViewLayout", nestedScrollViewLayout!!, parent),
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }
    }

    //约束布局示例
    private var constraintLayoutContainer: ConstraintLayoutSampleContainer? = null
    private fun addConstraintLayout(parent: ViewGroup) {
        if (constraintLayoutContainer == null) {
            constraintLayoutContainer = ConstraintLayoutSampleContainer(parent.context)
            parent.addView(
                createButtonToShowViewInContainer(
                    "约束布局相关示例", constraintLayoutContainer!!, parent),
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).setVerticalMargin()
            )
        }
    }


    //MotionLayout 示例
    private var motionLayoutContainer: MotionLayoutContainer? = null
    private fun addMotionLayoutContainer(parent: ViewGroup) {
        if (motionLayoutContainer == null) {
            motionLayoutContainer = MotionLayoutContainer(parent.context)
            parent.addView(
                createButtonToShowViewInContainer(
                    "MotionLayout 相关示例", motionLayoutContainer!!, parent),
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
        showView: View,
        parent: ViewGroup
    ): AppCompatButton {
        val padding = dp(20)
        return AppCompatButton(parent.context).apply {
            text = buttonName
            isAllCaps = false

            setPadding(padding, 0, padding, 0)
            setBackgroundResource(R.drawable.button_background_circle_corner)
            setTextColor(Color.WHITE)
            setOnClickListener {
                parent.visibility = View.VISIBLE
                parent.addView(
                    showView,
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                )

                parent.hideAllView()
                if (showView.visibility != View.VISIBLE) {
                    showView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun ViewGroup.hideAllView() {
        children?.forEach { child ->
            if (child.visibility != View.GONE) {
                child.visibility = View.GONE
            }
        }
    }

    private val defaultMargin = dp(2)
    private fun LinearLayout.LayoutParams.setVerticalMargin(margin: Int = defaultMargin): LinearLayout.LayoutParams {
        setMargins(0, margin, 0, margin)
        SwordLog.debug(tag, "defaultMargin: $defaultMargin")
        return this
    }
}