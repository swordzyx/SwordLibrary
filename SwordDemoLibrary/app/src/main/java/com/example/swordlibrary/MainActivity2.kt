package com.example.swordlibrary

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
import androidx.core.view.children
import com.example.swordlibrary.view.*
import com.sword.LogUtil
import com.sword.initWindowSize
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity2: AppCompatActivity() {
  private val tag = "MainActivity2"
  private var multiTouchView1: MultiTouchView1? = null
  private var multiTouchView2: MultiTouchView2? = null
  private var multiTouchView3: MultiTouchView3? = null
  private var multiTouchView4: MultiTouchView4? = null
  private var dragListenerGridView: DragListenerGridView? = null
  
  private var twoPager: TwoPager? = null
  
  private var contentContainer: FrameLayout? = null

  @SuppressLint("SetTextI18n", "InflateParams")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)
    val rootView = findViewById<LinearLayout>(R.id.rootView)
    contentContainer = findViewById(R.id.contentView)
    rootView.addView(Button(this).apply {
      text = "MultiTouchView1 单点触控"
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
    
    rootView.addView(Button(this).apply {
      text = "MultiTouchView2 接力型滑动"
      setOnClickListener {
        contentView.visibility = View.VISIBLE
        if (multiTouchView2 == null) {
          multiTouchView2 = MultiTouchView2(
            this@MainActivity2)
          contentView.addView(multiTouchView2,
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          )
        }

        hideAllView()
        if (multiTouchView2?.visibility != View.VISIBLE) {
          multiTouchView2?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

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
    
    initWindowSize(this)
  }

  private var dragToCollectLayout: DragToCollectLayout? = null
  @SuppressLint("SetTextI18n")
  private fun addDragToCollectLayout() {
    rootView.addView(Button(this).apply {
      text = "DragToCollectLayout"
      isAllCaps = false
      setOnClickListener {
        contentContainer?.visibility = View.VISIBLE

        if (dragToCollectLayout == null) {
          dragToCollectLayout = LayoutInflater.from(this@MainActivity2).inflate(R.layout.drag_to_collect_layout, null) as DragToCollectLayout
          contentContainer?.addView(
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
        contentContainer?.visibility = View.VISIBLE

        if (dragHelperGridView == null) {
          dragHelperGridView = LayoutInflater.from(this@MainActivity2).inflate(R.layout.drag_helper_grid_view, null) as DragHelperGridView
          contentContainer?.addView(
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
        contentContainer?.visibility = View.VISIBLE

        if (dragUpDownLayout == null) {
          dragUpDownLayout = LayoutInflater.from(this@MainActivity2).inflate(R.layout.drag_up_down, null) as DragUpDownLayout
          contentContainer?.addView(
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

  private fun hideAllView() {
    contentContainer?.children?.forEach { child -> 
      if (child.visibility != View.GONE) {
        child.visibility = View.GONE
      }
    }
  }

  override fun onBackPressed() {
    if (contentContainer?.visibility != View.GONE) {
      contentContainer?.visibility = View.GONE
    } else {
      super.onBackPressed()
    }
  }
}