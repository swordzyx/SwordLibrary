package com.sword.kotlinandroid.lesson

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sword.base.core.BasevView
import com.sword.kotlinandroid.R
import com.sword.kotlinandroid.entity.Lesson

class LessonActivity : AppCompatActivity(), BasevView<LessonPresenter>, Toolbar.OnMenuItemClickListener {
    private val lessonPresenter: LessonPresenter = LessonPresenter(this)

    private val lessonAdapter: LessonAdapter = LessonAdapter()

    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SWORD", "LessonActivity----onCreate")
        setContentView(R.layout.activity_lesson)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu_lesson)
        toolbar.setOnMenuItemClickListener(this)

        val recyclerView: RecyclerView = findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = lessonAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

        refreshLayout = findViewById(R.id.swipe_refresh_layout)
        refreshLayout.setOnRefreshListener {
            lessonPresenter.fetchData()
        }
        refreshLayout.isRefreshing = true

        lessonPresenter.fetchData()
    }

    fun showResult(lessons: ArrayList<Lesson>) {
        lessonAdapter.updateAndNotify(lessons)
    }

    override fun getPresenter(): LessonPresenter {
        return lessonPresenter
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        lessonPresenter.showPlayback()
        return false
    }
}